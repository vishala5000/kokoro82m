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
    private final KokoroTokenizer tokenizer;
    private final Map<String, float[]> voiceCache = new HashMap<>();
    private final Context ctx;

    public KokoroEngine(Context ctx) throws Exception {
        this.ctx = ctx.getApplicationContext();
        tokenizer = new KokoroTokenizer(ctx);

        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        opts.setIntraOpNumThreads(Math.max(2, Runtime.getRuntime().availableProcessors()));
        opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
        opts.addCPU(true);

        File model = new File(ctx.getFilesDir(), "kokoro/model.onnx");
        if (!model.exists()) copyAsset("kokoro/model.onnx", model);

        session = env.createSession(model.getAbsolutePath(), opts);
        Log.d("KokoroTTS", "Kokoro engine initialized");
    }

    /**
     * Synthesize speech. Language is auto-detected from voiceId.
     */
    public float[] synthesize(String text, String voiceId, float speed) throws Exception {
        // 1) Phonemize (language auto-detected from voiceId)
        String phonemes = Phonemizer.phonemize(text, voiceId);
        Log.d("KokoroTTS", "Voice: " + voiceId + " | Phonemes: " + phonemes);

        // 2) Tokenize
        long[] ids = tokenizer.encode(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");

        // 3) Load voice embedding (row selected by input length)
        float[] style = loadVoice(voiceId, ids.length);

        // 4) Build ONNX inputs
        OnnxTensor tIds   = OnnxTensor.createTensor(env, LongBuffer.wrap(ids), new long[]{1, ids.length});
        OnnxTensor tStyle = OnnxTensor.createTensor(env, FloatBuffer.wrap(style), new long[]{1, 256});
        OnnxTensor tSpeed = OnnxTensor.createTensor(env, FloatBuffer.wrap(new float[]{speed}), new long[]{1});

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", tIds);
        inputs.put("style",     tStyle);
        inputs.put("speed",     tSpeed);

        // 5) Run inference
        try (OrtSession.Result res = session.run(inputs)) {
            Object val = res.get(0).getValue();
            if (val instanceof float[][][]) return ((float[][][]) val)[0][0];
            if (val instanceof float[][])   return ((float[][]) val)[0];
            throw new IllegalStateException("Unexpected output: " + val.getClass().getName());
        } finally {
            tIds.close(); tStyle.close(); tSpeed.close();
        }
    }

    private synchronized float[] loadVoice(String id, int sequenceLength) throws Exception {
        String cacheKey = id + "_" + sequenceLength;
        if (voiceCache.containsKey(cacheKey)) return voiceCache.get(cacheKey);

        File f = new File(ctx.getFilesDir(), "kokoro/voices/" + id + ".bin");
        if (!f.exists()) copyAsset("kokoro/voices/" + id + ".bin", f);

        byte[] bytes = readAll(new FileInputStream(f));
        FloatBuffer fb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();

        int rowIndex = Math.min(Math.max(sequenceLength - 1, 0), 509);
        float[] allVoices = new float[510 * 256];
        fb.get(allVoices);

        float[] style = new float[256];
        System.arraycopy(allVoices, rowIndex * 256, style, 0, 256);

        voiceCache.put(cacheKey, style);
        return style;
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
