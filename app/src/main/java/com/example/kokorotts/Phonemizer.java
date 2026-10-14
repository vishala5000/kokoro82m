package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;

public class Phonemizer {
    
    private static final Map<String, String> EN_DICT = new HashMap<>();
    private static final Map<String, String> HI_DICT = new HashMap<>();

    static {
        // English - using ONLY characters from Kokoro's vocab
        // Note: no lowercase 'g' in vocab, use 'ɡ' (U+0261) instead
        EN_DICT.put("hello", "həloʊ");
        EN_DICT.put("world", "wɜld");
        EN_DICT.put("the", "ðə");
        EN_DICT.put("a", "ə");
        EN_DICT.put("is", "ɪz");
        EN_DICT.put("are", "ɑɹ");
        EN_DICT.put("you", "ju");
        EN_DICT.put("how", "haʊ");
        EN_DICT.put("today", "tədeɪ");
        EN_DICT.put("test", "tɛst");
        EN_DICT.put("kokoro", "koʊkɔɹoʊ");
        EN_DICT.put("text", "tɛkst");
        EN_DICT.put("to", "tu");
        EN_DICT.put("speech", "spiʧ");
        EN_DICT.put("this", "ðɪs");
        EN_DICT.put("that", "ðæt");
        EN_DICT.put("and", "ænd");
        EN_DICT.put("for", "fɔɹ");
        EN_DICT.put("with", "wɪð");
        EN_DICT.put("not", "nɒt");
        EN_DICT.put("yes", "jɛs");
        EN_DICT.put("no", "noʊ");
        EN_DICT.put("good", "ɡʊd");
        EN_DICT.put("bad", "bæd");
        EN_DICT.put("my", "maɪ");
        EN_DICT.put("your", "jɔɹ");
        EN_DICT.put("name", "neɪm");
        EN_DICT.put("what", "wɒt");
        EN_DICT.put("where", "wɛɹ");
        EN_DICT.put("when", "wɛn");
        EN_DICT.put("why", "waɪ");
        EN_DICT.put("who", "hu");
        EN_DICT.put("i", "aɪ");
        EN_DICT.put("me", "mi");
        EN_DICT.put("we", "wi");
        EN_DICT.put("he", "hi");
        EN_DICT.put("she", "ʃi");
        EN_DICT.put("it", "ɪt");
        EN_DICT.put("they", "ðeɪ");
        EN_DICT.put("can", "kæn");
        EN_DICT.put("will", "wɪl");
        EN_DICT.put("have", "hæv");
        EN_DICT.put("do", "du");
        EN_DICT.put("be", "bi");
        EN_DICT.put("am", "æm");
        EN_DICT.put("was", "wɒz");
        EN_DICT.put("from", "fɹɒm");
        EN_DICT.put("on", "ɒn");
        EN_DICT.put("in", "ɪn");
        EN_DICT.put("at", "æt");
        EN_DICT.put("by", "baɪ");
        EN_DICT.put("of", "ɒv");
        EN_DICT.put("an", "æn");
        EN_DICT.put("or", "ɔɹ");
        EN_DICT.put("if", "ɪf");
        EN_DICT.put("so", "soʊ");
        EN_DICT.put("up", "ʌp");
        EN_DICT.put("out", "aʊt");
        EN_DICT.put("about", "əbaʊt");
        EN_DICT.put("please", "pliːz");
        EN_DICT.put("thank", "θæŋk");
        EN_DICT.put("thanks", "θæŋks");
        EN_DICT.put("sorry", "sɒɹi");
        EN_DICT.put("welcome", "wɛlkəm");
        
        // Hindi - using ONLY characters from Kokoro's vocab
        HI_DICT.put("नमस्ते", "nəmasteː");
        HI_DICT.put("धन्यवाद", "d̪ʱənjiəʋɑːd̪");
        HI_DICT.put("हाँ", "ɦɑ̃ː");
        HI_DICT.put("नहीं", "nəɦĩː");
        HI_DICT.put("है", "hɛː");
        HI_DICT.put("हैं", "hɛ̃ː");
        HI_DICT.put("आप", "ɑːp");
        HI_DICT.put("कैसे", "kɛːseː");
        HI_DICT.put("हो", "ɦoː");
        HI_DICT.put("ठीक", "t̪ʰiːk");
        HI_DICT.put("भारत", "bʱɑːɹət̪");
        HI_DICT.put("हिंदी", "ɦɪnd̪iː");
        HI_DICT.put("अच्छा", "ətʃʰɑː");
        HI_DICT.put("धन्यवाद", "d̪ʱənjiəʋɑːd̪");
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
            if (ipa != null) {
                result.append(ipa);
            } else {
                result.append(englishFallback(word));
            }
            if (i < words.length - 1) result.append(" ");
        }
        return result.toString();
    }

    private static String englishFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char next = (i + 1 < word.length()) ? word.charAt(i + 1) : ' ';
            
            // Map to IPA using ONLY characters in Kokoro's vocab
            if (c == 'a') ipa.append(next == 'i' ? "eɪ" : (next == 'u' ? "ɔː" : "æ"));
            else if (c == 'e') ipa.append((next == 'a' || next == 'i') ? "iː" : (i == word.length() - 1 ? "" : "ɛ"));
            else if (c == 'i') ipa.append(next == 'e' ? "aɪ" : "ɪ");
            else if (c == 'o') ipa.append(next == 'e' ? "oʊ" : "ɒ");
            else if (c == 'u') ipa.append(next == 'e' ? "juː" : "ʌ");
            else if (c == 'g') ipa.append("ɡ");  // Use U+0261 (ɡ), NOT lowercase g
            else if (c == 't' && next == 'h') { ipa.append("θ"); i++; }
            else if (c == 's' && next == 'h') { ipa.append("ʃ"); i++; }
            else if (c == 'c' && next == 'h') { ipa.append("ʧ"); i++; }
            else if (c == 'c' && (next == 'e' || next == 'i')) ipa.append("s");
            else if (c == 'c') ipa.append("k");
            else if (c == 'n' && next == 'g') { ipa.append("ŋ"); i++; }
            else if (c == 'w') ipa.append("w");
            else if (c == 'y') ipa.append(i == 0 ? "j" : "i");
            else if (c == 'x') ipa.append("ks");
            else if (Character.isLetter(c)) ipa.append(c);
        }
        return ipa.toString();
    }

    private static String phonemizeHindi(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("[^\\u0900-\\u097F]", "");
            if (word.isEmpty()) continue;
            
            String ipa = HI_DICT.get(word);
            if (ipa != null) {
                result.append(ipa);
            } else {
                result.append(hindiFallback(word));
            }
            if (i < words.length - 1) result.append(" ");
        }
        return result.toString();
    }

    private static String hindiFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            // Map Devanagari to IPA using ONLY characters in Kokoro's vocab
            if (c == 'अ') ipa.append("ə");
            else if (c == 'आ' || c == 'ा') ipa.append("ɑː");
            else if (c == 'इ' || c == 'ि') ipa.append("ɪ");
            else if (c == 'ई' || c == 'ी') ipa.append("iː");
            else if (c == 'उ' || c == 'ु') ipa.append("ʊ");
            else if (c == 'ऊ' || c == 'ू') ipa.append("uː");
            else if (c == 'ए' || c == 'े') ipa.append("eː");
            else if (c == 'ऐ' || c == 'ै') ipa.append("ɛː");
            else if (c == 'ओ' || c == 'ो') ipa.append("oː");
            else if (c == 'औ' || c == 'ौ') ipa.append("ɔː");
            else if (c == 'क') ipa.append("k");
            else if (c == 'ख') ipa.append("kʰ");
            else if (c == 'ग') ipa.append("ɡ");  // U+0261, not ASCII g
            else if (c == 'घ') ipa.append("ɡʰ");
            else if (c == 'ङ') ipa.append("ŋ");
            else if (c == 'च') ipa.append("ʧ");
            else if (c == 'छ') ipa.append("ʧʰ");
            else if (c == 'ज') ipa.append("ʤ");
            else if (c == 'झ') ipa.append("ʤʰ");
            else if (c == 'ञ') ipa.append("ɲ");
            else if (c == 'ट') ipa.append("ʈ");
            else if (c == 'ठ') ipa.append("ʈʰ");
            else if (c == 'ड') ipa.append("ɖ");
            else if (c == 'ढ') ipa.append("ɖʰ");
            else if (c == 'ण') ipa.append("ɳ");
            else if (c == 'त') ipa.append("t̪");
            else if (c == 'थ') ipa.append("t̪ʰ");
            else if (c == 'द') ipa.append("d̪");
            else if (c == 'ध') ipa.append("d̪ʰ");
            else if (c == 'न') ipa.append("n");
            else if (c == 'प') ipa.append("p");
            else if (c == 'फ') ipa.append("pʰ");
            else if (c == 'ब') ipa.append("b");
            else if (c == 'भ') ipa.append("bʰ");
            else if (c == 'म') ipa.append("m");
            else if (c == 'य') ipa.append("j");
            else if (c == 'र') ipa.append("ɾ");
            else if (c == 'ल') ipa.append("l");
            else if (c == 'व') ipa.append("ʋ");
            else if (c == 'श') ipa.append("ʃ");
            else if (c == 'ष') ipa.append("ʂ");
            else if (c == 'स') ipa.append("s");
            else if (c == 'ह') ipa.append("ɦ");
            else if (c == 'ं') ipa.append("̃");
            else if (c == 'ः') ipa.append("h");
            else if (c == '्') {
                // Halant: suppress inherent vowel
                int len = ipa.length();
                if (len > 0 && ipa.charAt(len - 1) == 'ə') {
                    ipa.deleteCharAt(len - 1);
                }
            }
        }
        return ipa.toString();
    }
}
