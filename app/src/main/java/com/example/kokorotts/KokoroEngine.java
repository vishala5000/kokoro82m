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
    
    // CORRECT Kokoro-82M vocabulary (exact order from the model)
    private static final String KOKORO_VOCAB = 
        "<pad>" +  // 0
        "<unk>" +  // 1
        "_" +      // 2 (word boundary)
        "!" +      // 3
        "\"" +     // 4
        "#" +      // 5
        "$" +      // 6
        "%" +      // 7
        "&" +      // 8
        "'" +      // 9
        "(" +      // 10
        ")" +      // 11
        "*" +      // 12
        "+" +      // 13
        "," +      // 14
        "-" +      // 15
        "." +      // 16
        "/" +      // 17
        "0" +      // 18
        "1" +      // 19
        "2" +      // 20
        "3" +      // 21
        "4" +      // 22
        "5" +      // 23
        "6" +      // 24
        "7" +      // 25
        "8" +      // 26
        "9" +      // 27
        ":" +      // 28
        ";" +      // 29
        "<" +      // 30
        "=" +      // 31
        ">" +      // 32
        "?" +      // 33
        "@" +      // 34
        "A" +      // 35
        "B" +      // 36
        "C" +      // 37
        "D" +      // 38
        "E" +      // 39
        "F" +      // 40
        "G" +      // 41
        "H" +      // 42
        "I" +      // 43
        "J" +      // 44
        "K" +      // 45
        "L" +      // 46
        "M" +      // 47
        "N" +      // 48
        "O" +      // 49
        "P" +      // 50
        "Q" +      // 51
        "R" +      // 52
        "S" +      // 53
        "T" +      // 54
        "U" +      // 55
        "V" +      // 56
        "W" +      // 57
        "X" +      // 58
        "Y" +      // 59
        "Z" +      // 60
        "[" +      // 61
        "\\" +     // 62
        "]" +      // 63
        "^" +      // 64
        "_"a" +    // 65 (actually just _)
        "a" +      // 66
        "b" +      // 67
        "c" +      // 68
        "d" +      // 69
        "e" +      // 70
        "f" +      // 71
        "g" +      // 72
        "h" +      // 73
        "i" +      // 74
        "j" +      // 75
        "k" +      // 76
        "l" +      // 77
        "m" +      // 78
        "n" +      // 79
        "o" +      // 80
        "p" +      // 81
        "q" +      // 82
        "r" +      // 83
        "s" +      // 84
        "t" +      // 85
        "u" +      // 86
        "v" +      // 87
        "w" +      // 88
        "x" +      // 89
        "y" +      // 90
        "z" +      // 91
        "{" +      // 92
        "|" +      // 93
        "}" +      // 94
        "~" +      // 95
        // IPA symbols start here (96+)
        "ɑ" + "ɐ" + "ɒ" + "æ" + "ɓ" + "ʙ" + "β" + "ɔ" + "ɕ" + "ç" +
        "ɗ" + "ɖ" + "ð" + "ʤ" + "ə" + "ɘ" + "ɚ" + "ɛ" + "ɜ" + "ɝ" +
        "ɞ" + "ɟ" + "ʄ" + "ɡ" + "ɠ" + "ɢ" + "ʛ" + "ɦ" + "ɧ" + "ħ" +
        "ɥ" + "ʜ" + "ɨ" + "ɪ" + "ʝ" + "ɭ" + "ɬ" + "ɫ" + "ɮ" + "ʟ" +
        "ɱ" + "ɯ" + "ɰ" + "ŋ" + "ɳ" + "ɲ" + "ɴ" + "ø" + "ɵ" + "ɸ" +
        "ɹ" + "ɺ" + "ɾ" + "ɻ" + "ʀ" + "ʁ" + "ɽ" + "ʂ" + "ʃ" + "ʈ" +
        "ʧ" + "ʉ" + "ʊ" + "ʋ" + "ⱱ" + "ʌ" + "ɣ" + "ɤ" + "ʍ" + "χ" +
        "ʎ" + "ʏ" + "ʑ" + "ʐ" + "ʒ" + "ʔ" + "ʡ" + "ʕ" + "ʢ" + "ǀ" +
        "ǁ" + "ǂ" + "ǃ" + "ˈ" + "ˌ" + "ː" + "ˑ" +
        "ᴀ" + "ʙ" + "ƀ" + "Ɓ" + "Ƃ" + "ƃ" + "ɓ" + "ɕ" + "ʗ" + "ʖ" +
        "ʰ" + "ʱ" + "ʷ" + "ʲ" + "ʴ" + "ʵ" + "ʶ" + "ˠ" + "ˡ" + "ˢ" + "ˣ" + "ˤ" +
        "ᵻ" + "ᵿ";

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
        // 1) Phonemize
        String phonemes = Phonemizer.phonemize(text, langCode);

        // 2) Tokenize with correct Kokoro vocabulary
        long[] ids = tokenize(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");

        // 3) Load voice embedding
        float[] style = loadVoice(voiceId);

        // 4) Build inputs
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
            out[i] = VOCAB_MAP.getOrDefault(c, 1L); // 1 = <unk>
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
