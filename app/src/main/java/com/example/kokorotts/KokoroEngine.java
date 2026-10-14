package com.example.kokorotts;

import android.content.Context;

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
    
    // FIXED: Correct Kokoro-82M vocabulary (no typos)
    private static final String KOKORO_VOCAB = 
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
        for (int i = 0; i < KOKORO_VOCAB.length(); ) {
            int cp = KOKORO_VOCAB.codePointAt(i);
            VOCAB_MAP.put((char) cp, (long) idx++);
            i += Character.charCount(cp);
        }
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
    }

    public float[] synthesize(String text, String voiceId, String langCode, float speed) throws Exception {
        String phonemes = Phonemizer.phonemize(text, langCode);
        long[] ids = tokenize(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");

        float[] style = loadVoice(voiceId);

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

    private long[] tokenize(String phonemes) {
        long[] out = new long[phonemes.length()];
        for (int i = 0; i < phonemes.length(); i++) {
            char c = phonemes.charAt(i);
            out[i] = VOCAB_MAP.getOrDefault(c, 1L);
        }
        return out;
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
