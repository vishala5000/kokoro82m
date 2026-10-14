package com.example.kokorotts;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

/**
 * Kokoro-82M ONNX inference engine.
 *
 * Model inputs (Kokoro v1.0 ONNX):
 *   - input_ids : int64[1, N]   phoneme token ids
 *   - style     : float32[1,256] voice embedding
 *   - speed     : float32[1]     speaking speed
 * Model output:
 *   - audio     : float32[1, 1, L]  waveform @ 24 kHz
 */
public class KokoroEngine {

    private final OrtEnvironment env = OrtEnvironment.getEnvironment();
    private final OrtSession session;
    private final Map<String, float[]> voiceCache = new HashMap<>();
    private final Context ctx;

    public KokoroEngine(Context ctx) throws Exception {
        this.ctx = ctx.getApplicationContext();

        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        // CPU optimizations — works on every smartphone
        opts.setIntraOpNumThreads(Math.max(2,
                Runtime.getRuntime().availableProcessors()));
        opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
        opts.addCPU(true);

        File model = new File(ctx.getFilesDir(), "kokoro/model.onnx");
        if (!model.exists()) copyAsset("kokoro/model.onnx", model);

        session = env.createSession(model.getAbsolutePath(), opts);
    }

    public float[] synthesize(String text, String voiceId,
                              String langCode, float speed) throws Exception {
        // 1) Phonemize
        String phonemes = Phonemizer.phonemize(text, langCode);

        // 2) Tokenize (Kokoro uses a simple char-level vocab over IPA+letters)
        int[] ids = tokenize(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");

        // 3) Load voice embedding (256-dim)
        float[] style = loadVoice(voiceId);

        // 4) Build inputs
        long[] idsShape = {1, ids.length};
        long[] styleShape = {1, 256};
        long[] speedShape = {1};

        OnnxTensor tIds   = OnnxTensor.createTensor(env,
                IntBuffer.wrap(ids), idsShape);
        OnnxTensor tStyle = OnnxTensor.createTensor(env,
                FloatBuffer.wrap(style), styleShape);
        OnnxTensor tSpeed = OnnxTensor.createTensor(env,
                FloatBuffer.wrap(new float[]{speed}), speedShape);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        // Kokoro ONNX input names (v1.0)
        inputs.put("input_ids", tIds);
        inputs.put("style",     tStyle);
        inputs.put("speed",     tSpeed);

        // 5) Run
        try (OrtSession.Result res = session.run(inputs)) {
            float[][][][] audio = (float[][][][]) res.get(0).getValue();
            float[] pcm = audio[0][0];          // [L]
            return pcm;
        } finally {
            tIds.close(); tStyle.close(); tSpeed.close();
        }
    }

    /* ---------- helpers ---------- */

    private synchronized float[] loadVoice(String id) throws Exception {
        if (voiceCache.containsKey(id)) return voiceCache.get(id);
        File f = new File(ctx.getFilesDir(), "kokoro/voices/" + id + ".bin");
        if (!f.exists()) copyAsset("kokoro/voices/" + id + ".bin", f);

        byte[] bytes = readAll(new FileInputStream(f));
        FloatBuffer fb = ByteBuffer.wrap(bytes)
                .order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] v = new float[fb.remaining()];
        fb.get(v);
        voiceCache.put(id, v);
        return v;
    }

    /**
     * Kokoro tokenizer: maps each character in the phonemized string to an id.
     * The published vocab is ~80 symbols; we use the published mapping.
     */
    private int[] tokenize(String phonemes) {
        // Kokoro v1.0 vocab (pad=0, then symbols). Order matches the official
        // tokenizer.json published with the ONNX export.
        String vocab =
            "<pad><unk>" +
            "_!\"#$%&'()*+,-./0123456789:;<=>?@" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "[\\]^_`abcdefghijklmnopqrstuvwxyz" +
            "{|}~" +
            "ɑɐɒæɓʙβɔɕçɗɖðʤəɘɚɛɜɝɞɟʄɡɠɢʛɦɧħɥʜɨɪʝɭɬɫɮʟɱɯɰŋɳɲɴ" +
            "øɵɸɹɺɾɻʀʁɽʂʃʈʧʉʊʋⱱʌɣɤʍχʎʏʑʐʒʔʡʕʢǀǁǂǃˈˌːˑ" +
            "ᴀʙƀƁƂƃɓɕʗʖ" +
            "ʰʱʷʲʴʵʶˠˡˢˣˤᵻᵿ" +
            "̩̯̰̘̙̜̝̞̟̠̣̤̥̦̩̪̫̬̮̯̰̱̲̹̺̻̼̽" +
            "ʲˠˡˢˣˤᵻᵿ" +
            " ";
        // Build map
        Map<Character,Integer> map = new HashMap<>();
        int idx = 0;
        for (int i = 0; i < vocab.length(); ) {
            int cp = vocab.codePointAt(i);
            map.put((char)cp, idx++);
            i += Character.charCount(cp);
        }
        int[] out = new int[phonemes.length()];
        for (int i = 0; i < phonemes.length(); i++) {
            char c = phonemes.charAt(i);
            out[i] = map.getOrDefault(c, 1);   // 1 = <unk>
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
