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
        Log.d("KokoroTTS", "Kokoro engine initialized successfully");
    }

    public float[] synthesize(String text, String voiceId, String langCode, float speed) throws Exception {
        // 1) Phonemize
        String phonemes = Phonemizer.phonemize(text, langCode);
        Log.d("KokoroTTS", "Phonemes: " + phonemes);

        // 2) Tokenize with real Kokoro tokenizer
        long[] ids = tokenizer.encode(phonemes);
        if (ids.length == 0) throw new IllegalStateException("Empty token sequence");
        Log.d("KokoroTTS", "Token count: " + ids.length);

        // 3) Load voice embedding (row selected by input length for perfect quality)
        float[] style = loadVoice(voiceId, ids.length);

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
                throw new IllegalStateException("Unexpected output shape: " + val.getClass().getName());
            }
            return pcm;
        } finally {
            tIds.close();
            tStyle.close();
            tSpeed.close();
        }
    }

    /**
     * Load voice embedding. Kokoro voices are [510, 256] matrices.
     * We pick the row matching the input sequence length for best quality.
     */
    private synchronized float[] loadVoice(String id, int sequenceLength) throws Exception {
        String cacheKey = id + "_" + sequenceLength;
        if (voiceCache.containsKey(cacheKey)) return voiceCache.get(cacheKey);

        File f = new File(ctx.getFilesDir(), "kokoro/voices/" + id + ".bin");
        if (!f.exists()) copyAsset("kokoro/voices/" + id + ".bin", f);

        byte[] bytes = readAll(new FileInputStream(f));
        FloatBuffer fb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();

        // Pick row matching input length (0-indexed, max 509)
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
