package com.example.kokorotts;

public class Phonemizer {

    public static String phonemize(String text, String voiceId) {
        if (text == null || text.trim().isEmpty()) return "";
        text = text.trim();

        // Auto-detect language from voice ID
        if (voiceId.startsWith("hm_") || voiceId.startsWith("hf_")) {
            return phonemizeHindi(text);
        } else {
            return phonemizeEnglish(text);
        }
    }

    private static String phonemizeEnglish(String text) {
        // Just lowercase and return - let Kokoro's tokenizer handle it
        return text.toLowerCase().replaceAll("[^a-z0-9\\s.,!?;:']", " ");
    }

    private static String phonemizeHindi(String text) {
        // Just return as-is - let Kokoro's tokenizer handle it
        return text.replaceAll("[^\\u0900-\\u097F0-9\\s.,!?;:]", " ");
    }
}
