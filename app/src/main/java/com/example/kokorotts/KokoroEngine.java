package com.example.kokorotts;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.util.HashMap;
import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

public class KokoroEngine {

    private final OrtEnvironment env = OrtEnvironment.getEnvironment();
    private final OrtSession session;
    private final Map<String, float[]> voiceCache = new HashMap<>();
    private final Context ctx;
    
    // EXACT Kokoro-82M vocabulary order
    private static final String VOCAB = 
        "<pad><unk>_" +
        "!\"#$%&'()*+,-./0123456789:;<=>?@" +
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "[\\]^_`abcdefghijklmnopqrstuvwxyz" +
        "{|}~" +
        "ɑɐɒæɓʙβɔɕçɗɖðʤəɘɚɛɜɝɞɟʄɡɠɢʛɦɧħɥʜɨɪʝɭɬɫɮʟɱɯɰŋɳɲɴ" +
        "øɵɸɹɺɾɻʀʁɽʂʃʈʧʉʊʋⱱʌɣɤʍχʎʏʑʐʒʔʡʕʢǀǁǂǃˈˌːˑ" +
        "ᴀʙƀƁƂƃɓɕʗʖ" +
        "ʰʱʷʲʴʵʶˠˡˢˣˤᵻᵿ";

    private static final Map<Character, Long> VOCAB_MAP = new HashMap<>();
    
    static {
        int idx = 0;
        for (int i = 0; i < VOCAB.length(); ) {
            int cp = VOCAB.codePointAt(i);
            VOCAB_MAP.put((char) cp, (long) idx++);
            i += Character.charCount(cp);
        }
        Log.d("KokoroTTS", "Vocab loaded, size: " + VOCAB_MAP.size());
    }

    public KokoroEngine(Context ctx) throws Exception {
        this.ctx = ctx.getApplicationContext();

        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        opts.setIntraOpNumThreads(Math.max(2, Runtime.getRuntime().availableProcessors()));
        opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
        opts.addCPU(true);

        File model = new File(ctx.getFilesDir(), "kokoro/model.onnx");
        if (!model.exists()) copyAsset("kokoro/model.onnx", model);

        session = env.createSession(model.getAbsolutePath(), opts);
        Log.d("KokoroTTS", "Model loaded successfully");
    }

    public float[] synthesize(String text, String voiceId, String langCode, float speed) throws Exception {
        // 1) Phonemize to space-separated IPA
        String phonemes = Phonemizer.phonemize(text, langCode);
        Log.d("KokoroTTS", "Phonemes: " + phonemes);

        // 2) Tokenize: split by space, map each token to ID
        long[] ids = tokenize(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");

        // 3) Load voice embedding
        float[] style = loadVoice(voiceId);

        // 4) Build ONNX inputs
        long[] idsShape = {1, ids.length};
        long[] styleShape = {1, 256};
        long[] speedShape = {1};

        OnnxTensor tIds   = OnnxTensor.createTensor(env, LongBuffer.wrap(ids), idsShape);
        OnnxTensor tStyle = OnnxTensor.createTensor(env, FloatBuffer.wrap(style), styleShape);
        OnnxTensor tSpeed = OnnxTensor.createTensor(env, FloatBuffer.wrap(new float[]{speed}), speedShape);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", tIds);
        inputs.put("style",     tStyle);
        inputs.put("speed",     tSpeed);

        // 5) Run inference
        try (OrtSession.Result res = session.run(inputs)) {
            Object val = res.get(0).getValue();
            float[] pcm;
            if (val instanceof float[][][]) {
                pcm = ((float[][][]) val)[0][0];
            } else if (val instanceof float[][]) {
                pcm = ((float[][]) val)[0];
            } else {
                throw new IllegalStateException("Unexpected output tensor shape: " + val.getClass().getName());
            }
            return pcm;
        } finally {
            tIds.close();
            tStyle.close();
            tSpeed.close();
        }
    }

    private long[] tokenize(String phonemes) {
        String[] tokens = phonemes.split("\\s+");
        long[] ids = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.length() == 1) {
                ids[i] = VOCAB_MAP.getOrDefault(token.charAt(0), 1L);
            } else {
                ids[i] = mapMultiChar(token);
            }
        }
        return ids;
    }

    private long mapMultiChar(String token) {
        if (token.equals("ʧ")) return VOCAB_MAP.getOrDefault('ʧ', 1L);
        if (token.equals("ʤ")) return VOCAB_MAP.getOrDefault('ʤ', 1L);
        if (token.equals("kʰ")) return VOCAB_MAP.getOrDefault('k', 1L);
        if (token.equals("ɡʱ")) return VOCAB_MAP.getOrDefault('ɡ', 1L);
        if (token.equals("t̪ʰ")) return VOCAB_MAP.getOrDefault('t', 1L);
        if (token.equals("d̪ʱ")) return VOCAB_MAP.getOrDefault('d', 1L);
        if (token.equals("t̪")) return VOCAB_MAP.getOrDefault('t', 1L);
        if (token.equals("d̪")) return VOCAB_MAP.getOrDefault('d', 1L);
        if (token.equals("ʧʰ")) return VOCAB_MAP.getOrDefault('ʧ', 1L);
        if (token.equals("ʤʱ")) return VOCAB_MAP.getOrDefault('ʤ', 1L);
        if (token.equals("ʈʰ")) return VOCAB_MAP.getOrDefault('ʈ', 1L);
        if (token.equals("ɖʱ")) return VOCAB_MAP.getOrDefault('ɖ', 1L);
        if (token.equals("pʰ")) return VOCAB_MAP.getOrDefault('p', 1L);
        if (token.equals("bʱ")) return VOCAB_MAP.getOrDefault('b', 1L);
        
        return VOCAB_MAP.getOrDefault(token.charAt(0), 1L);
    }

    private synchronized float[] loadVoice(String id) throws Exception {
        if (voiceCache.containsKey(id)) return voiceCache.get(id);
        File f = new File(ctx.getFilesDir(), "kokoro/voices/" + id + ".bin");
        if (!f.exists()) copyAsset("kokoro/voices/" + id + ".bin", f);

        byte[] bytes = readAll(new FileInputStream(f));
        FloatBuffer fb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        
        float[] v = new float[256];
        fb.get(v);
        
        voiceCache.put(id, v);
        return v;
    }

    private void copyAsset(String asset, File dst) throws Exception {
        dst.getParentFile().mkdirs();
        try (InputStream is = ctx.getAssets().open(asset);
             FileOutputStream os = new FileOutputStream(dst)) {
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = is.read(buf)) > 0) os.write(buf, 0, n);
        }
    }

    private byte[] readAll(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[64 * 1024];
        int n;
        while ((n = is.read(buf)) > 0) bos.write(buf, 0, n);
        return bos.toByteArray();
    }
}
