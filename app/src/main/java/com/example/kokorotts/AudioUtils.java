package com.example.kokorotts;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioUtils {

    public static short[] floatToPcm16(float[] in) {
        short[] out = new short[in.length];
        for (int i = 0; i < in.length; i++) {
            float s = Math.max(-1f, Math.min(1f, in[i]));
            out[i] = (short)(s * 32767f);
        }
        return out;
    }

    public static byte[] toWav(float[] pcm, int sampleRate) {
        short[] pcm16 = floatToPcm16(pcm);
        int byteRate = sampleRate * 2;           // mono, 16-bit
        int dataSize = pcm16.length * 2;

        ByteBuffer bb = ByteBuffer.allocate(44 + dataSize)
                .order(ByteOrder.LITTLE_ENDIAN);

        // RIFF header
        bb.put("RIFF".getBytes());
        bb.putInt(36 + dataSize);
        bb.put("WAVE".getBytes());

        // fmt chunk
        bb.put("fmt ".getBytes());
        bb.putInt(16);
        bb.putShort((short)1);                   // PCM
        bb.putShort((short)1);                   // mono
        bb.putInt(sampleRate);
        bb.putInt(byteRate);
        bb.putShort((short)2);                   // block align
        bb.putShort((short)16);                  // bits per sample

        // data chunk
        bb.put("data".getBytes());
        bb.putInt(dataSize);
        for (short s : pcm16) bb.putShort(s);

        return bb.array();
    }
}
