package com.example.kokorotts;

/**
 * Thin wrapper around the JNI phonemizer.
 * langCode: "a" = American English, "h" = Hindi (matches Kokoro).
 */
public class Phonemizer {
    static { System.loadLibrary("espeak_jni"); }

    public static native String nativePhonemize(String text, String lang);

    public static String phonemize(String text, String lang) {
        return nativePhonemize(text, lang);
    }
}
