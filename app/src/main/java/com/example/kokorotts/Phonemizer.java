package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;

public class Phonemizer {
    private static final Map<String, String> EN_DICT = new HashMap<>();
    private static final Map<String, String> HI_DICT = new HashMap<>();

    static {
        // Common English
        EN_DICT.put("hello", "h ɛ l oʊ");
        EN_DICT.put("world", "w ɜː l d");
        EN_DICT.put("the", "ð ə");
        EN_DICT.put("a", "ə");
        EN_DICT.put("is", "ɪ z");
        EN_DICT.put("are", "ɑː r");
        EN_DICT.put("you", "j uː");
        EN_DICT.put("how", "h aʊ");
        EN_DICT.put("today", "t ə d eɪ");
        EN_DICT.put("test", "t ɛ s t");
        EN_DICT.put("kokoro", "k oʊ k ɔː r oʊ");
        EN_DICT.put("text", "t ɛ k s t");
        EN_DICT.put("to", "t uː");
        EN_DICT.put("speech", "s p iː tʃ");
        
        // Common Hindi
        HI_DICT.put("नमस्ते", "n ə m ʌ s t eː");
        HI_DICT.put("धन्यवाद", "d̪ʱ ə n j ə ʋ ɑː d̪");
        HI_DICT.put("हाँ", "ɦ ɑ̃ː");
        HI_DICT.put("नहीं", "n ə h ɪ̃ː");
        HI_DICT.put("है", "h ɛː");
        HI_DICT.put("हैं", "h ɛ̃ː");
        HI_DICT.put("आप", "ɑː p");
        HI_DICT.put("कैसे", "k ɛː s eː");
        HI_DICT.put("हो", "ɦ oː");
        HI_DICT.put("ठीक", "t̪ʰ iː k");
        HI_DICT.put("भारत", "bʱ ɑː r ə t̪");
        HI_DICT.put("हिंदी", "ɦ ɪ n d̪ iː");
    }

    public static String phonemize(String text, String langCode) {
        if (text == null || text.trim().isEmpty()) return "";
        text = text.toLowerCase().trim();
        return langCode.equals("a") ? phonemizeEnglish(text) : phonemizeHindi(text);
    }

    private static String phonemizeEnglish(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("[^a-z']", "");
            if (word.isEmpty()) continue;
            
            String ipa = EN_DICT.get(word);
            result.append(ipa != null ? ipa : englishFallback(word));
            if (i < words.length - 1) result.append(" ");
        }
        return result.toString();
    }

    private static String englishFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char next = (i + 1 < word.length()) ? word.charAt(i + 1) : ' ';
            
            if (c == 'a') ipa.append(next == 'i' ? "eɪ " : (next == 'u' ? "ɔː " : "æ "));
            else if (c == 'e') ipa.append(next == 'a' || next == 'i' ? "iː " : (i == word.length() - 1 ? "" : "ɛ "));
            else if (c == 'i') ipa.append(next == 'e' ? "aɪ " : "ɪ ");
            else if (c == 'o') ipa.append(next == 'e' ? "oʊ " : "ɒ ");
            else if (c == 'u') ipa.append(next == 'e' ? "juː " : "ʌ ");
            else if (c == 't' && next == 'h') { ipa.append("θ "); i++; }
            else if (c == 's' && next == 'h') { ipa.append("ʃ "); i++; }
            else if (c == 'c' && next == 'h') { ipa.append("ʧ "); i++; }
            else if (c == 'c' && (next == 'e' || next == 'i')) ipa.append("s ");
            else if (c == 'c') ipa.append("k ");
            else if (c == 'g' && (next == 'e' || next == 'i')) ipa.append("ʤ ");
            else if (c == 'g') ipa.append("ɡ ");
            else if (c == 'n' && next == 'g') { ipa.append("ŋ "); i++; }
            else if (c == 'w') ipa.append("w ");
            else if (c == 'y') ipa.append(i == 0 ? "j " : "i ");
            else if (c == 'x') ipa.append("k s ");
            else if (Character.isLetter(c)) ipa.append(c).append(" ");
        }
        return ipa.toString().trim();
    }

    private static String phonemizeHindi(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("[^\\u0900-\\u097F]", "");
            if (word.isEmpty()) continue;
            
            String ipa = HI_DICT.get(word);
            result.append(ipa != null ? ipa : hindiFallback(word));
            if (i < words.length - 1) result.append(" ");
        }
        return result.toString();
    }

    private static String hindiFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c == 'अ') ipa.append("ə ");
            else if (c == 'आ' || c == 'ा') ipa.append("ɑː ");
            else if (c == 'इ' || c == 'ि') ipa.append("ɪ ");
            else if (c == 'ई' || c == 'ी') ipa.append("iː ");
            else if (c == 'उ' || c == 'ु') ipa.append("ʊ ");
            else if (c == 'ऊ' || c == 'ू') ipa.append("uː ");
            else if (c == 'ए' || c == 'े') ipa.append("eː ");
            else if (c == 'ऐ' || c == 'ै') ipa.append("ɛː ");
            else if (c == 'ओ' || c == 'ो') ipa.append("oː ");
            else if (c == 'औ' || c == 'ौ') ipa.append("ɔː ");
            else if (c == 'क') ipa.append("k ");
            else if (c == 'ख') ipa.append("kʰ ");
            else if (c == 'ग') ipa.append("ɡ ");
            else if (c == 'घ') ipa.append("ɡʱ ");
            else if (c == 'ङ') ipa.append("ŋ ");
            else if (c == 'च') ipa.append("ʧ ");
            else if (c == 'छ') ipa.append("ʧʰ ");
            else if (c == 'ज') ipa.append("ʤ ");
            else if (c == 'झ') ipa.append("ʤʱ ");
            else if (c == 'ञ') ipa.append("ɲ ");
            else if (c == 'ट') ipa.append("ʈ ");
            else if (c == 'ठ') ipa.append("ʈʰ ");
            else if (c == 'ड') ipa.append("ɖ ");
            else if (c == 'ढ') ipa.append("ɖʱ ");
            else if (c == 'ण') ipa.append("ɳ ");
            else if (c == 'त') ipa.append("t̪ ");
            else if (c == 'थ') ipa.append("t̪ʰ ");
            else if (c == 'द') ipa.append("d̪ ");
            else if (c == 'ध') ipa.append("d̪ʱ ");
            else if (c == 'न') ipa.append("n ");
            else if (c == 'प') ipa.append("p ");
            else if (c == 'फ') ipa.append("pʰ ");
            else if (c == 'ब') ipa.append("b ");
            else if (c == 'भ') ipa.append("bʱ ");
            else if (c == 'म') ipa.append("m ");
            else if (c == 'य') ipa.append("j ");
            else if (c == 'र') ipa.append("ɾ ");
            else if (c == 'ल') ipa.append("l ");
            else if (c == 'व') ipa.append("ʋ ");
            else if (c == 'श') ipa.append("ʃ ");
            else if (c == 'ष') ipa.append("ʂ ");
            else if (c == 'स') ipa.append("s ");
            else if (c == 'ह') ipa.append("ɦ ");
            else if (c == 'ं') ipa.append("̃ ");
            else if (c == 'ः') ipa.append("h ");
            else if (c == '्') {
                int len = ipa.length();
                if (len > 0 && ipa.charAt(len - 1) == ' ') ipa.deleteCharAt(len - 1);
            }
        }
        return ipa.toString().trim();
    }
}
