package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight Grapheme-to-Phoneme (G2P) converter for Kokoro TTS.
 * Converts English and Hindi text into IPA phonemes for natural-sounding speech.
 */
public class Phonemizer {

    // Comprehensive English word-to-IPA dictionary (common words)
    private static final Map<String, String> ENGLISH_DICT = new HashMap<>();
    
    // Hindi word-to-IPA dictionary (common words)
    private static final Map<String, String> HINDI_DICT = new HashMap<>();

    static {
        // Initialize English dictionary
        initEnglishDict();
        // Initialize Hindi dictionary
        initHindiDict();
    }

    public static String phonemize(String text, String langCode) {
        if (text == null || text.trim().isEmpty()) return "";
        
        // Normalize text
        text = text.toLowerCase().trim();
        
        if (langCode.equals("a")) {
            return phonemizeEnglish(text);
        } else if (langCode.equals("h")) {
            return phonemizeHindi(text);
        }
        
        return text;
    }

    private static String phonemizeEnglish(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            
            // Remove punctuation for lookup
            String cleanWord = word.replaceAll("[^a-z']", "");
            
            if (cleanWord.isEmpty()) {
                result.append(word);
                continue;
            }
            
            // Check dictionary first
            String ipa = ENGLISH_DICT.get(cleanWord);
            
            if (ipa != null) {
                result.append(ipa);
            } else {
                // Rule-based fallback for unknown words
                result.append(englishG2PFallback(cleanWord));
            }
            
            // Add space between words
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        
        return result.toString();
    }

    private static String phonemizeHindi(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String cleanWord = word.replaceAll("[^\\u0900-\\u097F]", "");
            
            if (cleanWord.isEmpty()) {
                result.append(word);
                continue;
            }
            
            // Check dictionary first
            String ipa = HINDI_DICT.get(cleanWord);
            
            if (ipa != null) {
                result.append(ipa);
            } else {
                // Rule-based fallback for Hindi
                result.append(hindiG2PFallback(cleanWord));
            }
            
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        
        return result.toString();
    }

    private static String englishG2PFallback(String word) {
        // Simple English G2P rules
        StringBuilder ipa = new StringBuilder();
        
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char next = (i + 1 < word.length()) ? word.charAt(i + 1) : ' ';
            
            // Vowel sounds
            if (c == 'a') {
                if (next == 'i') ipa.append("eɪ");
                else if (next == 'u') ipa.append("ɔː");
                else if (i > 0 && word.charAt(i-1) == 'e') ipa.append("eɪ");
                else ipa.append("æ");
            } else if (c == 'e') {
                if (next == 'a') ipa.append("iː");
                else if (next == 'i') ipa.append("iː");
                else if (i == word.length() - 1) ipa.append(""); // silent e
                else ipa.append("ɛ");
            } else if (c == 'i') {
                if (next == 'e') ipa.append("aɪ");
                else ipa.append("ɪ");
            } else if (c == 'o') {
                if (next == 'e') ipa.append("oʊ");
                else ipa.append("ɒ");
            } else if (c == 'u') {
                if (next == 'e') ipa.append("juː");
                else ipa.append("ʌ");
            }
            // Consonants
            else if (c == 't') {
                if (next == 'h') { ipa.append("θ"); i++; }
                else ipa.append("t");
            } else if (c == 's') {
                if (next == 'h') { ipa.append("ʃ"); i++; }
                else ipa.append("s");
            } else if (c == 'c') {
                if (next == 'h') { ipa.append("tʃ"); i++; }
                else if (next == 'e' || next == 'i') ipa.append("s");
                else ipa.append("k");
            } else if (c == 'g') {
                if (next == 'h') { ipa.append("ɡ"); i++; }
                else if (next == 'e' || next == 'i') ipa.append("dʒ");
                else ipa.append("ɡ");
            } else if (c == 'n' && next == 'g') {
                ipa.append("ŋ");
                i++;
            } else if (c == 'w') {
                ipa.append("w");
            } else if (c == 'y') {
                if (i == 0) ipa.append("j");
                else ipa.append("i");
            } else if (c == 'x') {
                ipa.append("ks");
            } else if (c == 'q') {
                ipa.append("kw");
            } else if (Character.isLetter(c)) {
                ipa.append(c);
            }
        }
        
        return ipa.toString();
    }

    private static String hindiG2PFallback(String word) {
        // Hindi Devanagari to IPA conversion
        StringBuilder ipa = new StringBuilder();
        
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            // Vowels (स्वर)
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
            // Consonants (व्यंजन)
            else if (c == 'क') ipa.append("k");
            else if (c == 'ख') ipa.append("kʰ");
            else if (c == 'ग') ipa.append("ɡ");
            else if (c == 'घ') ipa.append("ɡʱ");
            else if (c == 'ङ') ipa.append("ŋ");
            else if (c == 'च') ipa.append("tʃ");
            else if (c == 'छ') ipa.append("tʃʰ");
            else if (c == 'ज') ipa.append("dʒ");
            else if (c == 'झ') ipa.append("dʒʱ");
            else if (c == 'ञ') ipa.append("ɲ");
            else if (c == 'ट') ipa.append("ʈ");
            else if (c == 'ठ') ipa.append("ʈʰ");
            else if (c == 'ड') ipa.append("ɖ");
            else if (c == 'ढ') ipa.append("ɖʱ");
            else if (c == 'ण') ipa.append("ɳ");
            else if (c == 'त') ipa.append("t̪");
            else if (c == 'थ') ipa.append("t̪ʰ");
            else if (c == 'द') ipa.append("d̪");
            else if (c == 'ध') ipa.append("d̪ʱ");
            else if (c == 'न') ipa.append("n");
            else if (c == 'प') ipa.append("p");
            else if (c == 'फ') ipa.append("pʰ");
            else if (c == 'ब') ipa.append("b");
            else if (c == 'भ') ipa.append("bʱ");
            else if (c == 'म') ipa.append("m");
            else if (c == 'य') ipa.append("j");
            else if (c == 'र') ipa.append("ɾ");
            else if (c == 'ल') ipa.append("l");
            else if (c == 'व') ipa.append("ʋ");
            else if (c == 'श') ipa.append("ʃ");
            else if (c == 'ष') ipa.append("ʂ");
            else if (c == 'स') ipa.append("s");
            else if (c == 'ह') ipa.append("ɦ");
            // Special characters
            else if (c == '्') {
                // Virama (halant) - suppresses inherent vowel
                if (ipa.length() > 0 && ipa.charAt(ipa.length()-1) == 'ə') {
                    ipa.deleteCharAt(ipa.length()-1);
                }
            } else if (c == 'ं') ipa.append("̃"); // Anusvara
            else if (c == 'ः') ipa.append("h"); // Visarga
        }
        
        return ipa.toString();
    }

    private static void initEnglishDict() {
        // Common English words with their IPA pronunciations
        ENGLISH_DICT.put("hello", "hɛloʊ");
        ENGLISH_DICT.put("world", "wɜːld");
        ENGLISH_DICT.put("the", "ðə");
        ENGLISH_DICT.put("a", "ə");
        ENGLISH_DICT.put("an", "ən");
        ENGLISH_DICT.put("is", "ɪz");
        ENGLISH_DICT.put("are", "ɑːr");
        ENGLISH_DICT.put("was", "wɒz");
        ENGLISH_DICT.put("were", "wɜːr");
        ENGLISH_DICT.put("be", "biː");
        ENGLISH_DICT.put("been", "bɪn");
        ENGLISH_DICT.put("being", "biːɪŋ");
        ENGLISH_DICT.put("have", "hæv");
        ENGLISH_DICT.put("has", "hæz");
        ENGLISH_DICT.put("had", "hæd");
        ENGLISH_DICT.put("do", "duː");
        ENGLISH_DICT.put("does", "dʌz");
        ENGLISH_DICT.put("did", "dɪd");
        ENGLISH_DICT.put("will", "wɪl");
        ENGLISH_DICT.put("would", "wʊd");
        ENGLISH_DICT.put("could", "kʊd");
        ENGLISH_DICT.put("should", "ʃʊd");
        ENGLISH_DICT.put("may", "meɪ");
        ENGLISH_DICT.put("might", "maɪt");
        ENGLISH_DICT.put("must", "mʌst");
        ENGLISH_DICT.put("can", "kæn");
        ENGLISH_DICT.put("this", "ðɪs");
        ENGLISH_DICT.put("that", "ðæt");
        ENGLISH_DICT.put("these", "ðiːz");
        ENGLISH_DICT.put("those", "ðoʊz");
        ENGLISH_DICT.put("i", "aɪ");
        ENGLISH_DICT.put("you", "juː");
        ENGLISH_DICT.put("he", "hiː");
        ENGLISH_DICT.put("she", "ʃiː");
        ENGLISH_DICT.put("it", "ɪt");
        ENGLISH_DICT.put("we", "wiː");
        ENGLISH_DICT.put("they", "ðeɪ");
        ENGLISH_DICT.put("me", "miː");
        ENGLISH_DICT.put("him", "hɪm");
        ENGLISH_DICT.put("her", "hɜːr");
        ENGLISH_DICT.put("us", "ʌs");
        ENGLISH_DICT.put("them", "ðɛm");
        ENGLISH_DICT.put("my", "maɪ");
        ENGLISH_DICT.put("your", "jɔːr");
        ENGLISH_DICT.put("his", "hɪz");
        ENGLISH_DICT.put("our", "aʊər");
        ENGLISH_DICT.put("their", "ðɛr");
        ENGLISH_DICT.put("what", "wɒt");
        ENGLISH_DICT.put("which", "wɪtʃ");
        ENGLISH_DICT.put("who", "huː");
        ENGLISH_DICT.put("whom", "huːm");
        ENGLISH_DICT.put("where", "wɛr");
        ENGLISH_DICT.put("when", "wɛn");
        ENGLISH_DICT.put("why", "waɪ");
        ENGLISH_DICT.put("how", "haʊ");
        ENGLISH_DICT.put("all", "ɔːl");
        ENGLISH_DICT.put("each", "iːtʃ");
        ENGLISH_DICT.put("every", "ɛvri");
        ENGLISH_DICT.put("both", "boʊθ");
        ENGLISH_DICT.put("few", "fjuː");
        ENGLISH_DICT.put("more", "mɔːr");
        ENGLISH_DICT.put("other", "ʌðər");
        ENGLISH_DICT.put("some", "sʌm");
        ENGLISH_DICT.put("such", "sʌtʃ");
        ENGLISH_DICT.put("no", "noʊ");
        ENGLISH_DICT.put("not", "nɒt");
        ENGLISH_DICT.put("only", "oʊnli");
        ENGLISH_DICT.put("own", "oʊn");
        ENGLISH_DICT.put("same", "seɪm");
        ENGLISH_DICT.put("so", "soʊ");
        ENGLISH_DICT.put("than", "ðæn");
        ENGLISH_DICT.put("too", "tuː");
        ENGLISH_DICT.put("very", "vɛri");
        ENGLISH_DICT.put("just", "dʒʌst");
        ENGLISH_DICT.put("because", "bɪkɒz");
        ENGLISH_DICT.put("as", "æz");
        ENGLISH_DICT.put("until", "ʌntɪl");
        ENGLISH_DICT.put("while", "waɪl");
        ENGLISH_DICT.put("of", "ɒv");
        ENGLISH_DICT.put("at", "æt");
        ENGLISH_DICT.put("by", "baɪ");
        ENGLISH_DICT.put("for", "fɔːr");
        ENGLISH_DICT.put("with", "wɪð");
        ENGLISH_DICT.put("about", "əbaʊt");
        ENGLISH_DICT.put("against", "əɡɛnst");
        ENGLISH_DICT.put("between", "bɪtwiːn");
        ENGLISH_DICT.put("through", "θruː");
        ENGLISH_DICT.put("during", "djʊərɪŋ");
        ENGLISH_DICT.put("before", "bɪfɔːr");
        ENGLISH_DICT.put("after", "ɑːftər");
        ENGLISH_DICT.put("above", "əbʌv");
        ENGLISH_DICT.put("below", "bɪloʊ");
        ENGLISH_DICT.put("to", "tuː");
        ENGLISH_DICT.put("from", "frɒm");
        ENGLISH_DICT.put("up", "ʌp");
        ENGLISH_DICT.put("down", "daʊn");
        ENGLISH_DICT.put("in", "ɪn");
        ENGLISH_DICT.put("out", "aʊt");
        ENGLISH_DICT.put("on", "ɒn");
        ENGLISH_DICT.put("off", "ɒf");
        ENGLISH_DICT.put("over", "oʊvər");
        ENGLISH_DICT.put("under", "ʌndər");
        ENGLISH_DICT.put("again", "əɡɛn");
        ENGLISH_DICT.put("and", "ænd");
        ENGLISH_DICT.put("but", "bʌt");
        ENGLISH_DICT.put("or", "ɔːr");
        ENGLISH_DICT.put("if", "ɪf");
        ENGLISH_DICT.put("then", "ðɛn");
        ENGLISH_DICT.put("yes", "jɛs");
        ENGLISH_DICT.put("please", "pliːz");
        ENGLISH_DICT.put("thank", "θæŋk");
        ENGLISH_DICT.put("thanks", "θæŋks");
        ENGLISH_DICT.put("good", "ɡʊd");
        ENGLISH_DICT.put("bad", "bæd");
        ENGLISH_DICT.put("great", "ɡreɪt");
        ENGLISH_DICT.put("small", "smɔːl");
        ENGLISH_DICT.put("big", "bɪɡ");
        ENGLISH_DICT.put("new", "njuː");
        ENGLISH_DICT.put("old", "oʊld");
        ENGLISH_DICT.put("first", "fɜːrst");
        ENGLISH_DICT.put("last", "lɑːst");
        ENGLISH_DICT.put("long", "lɒŋ");
        ENGLISH_DICT.put("short", "ʃɔːrt");
        ENGLISH_DICT.put("high", "haɪ");
        ENGLISH_DICT.put("low", "loʊ");
        ENGLISH_DICT.put("right", "raɪt");
        ENGLISH_DICT.put("left", "lɛft");
        ENGLISH_DICT.put("early", "ɜːrli");
        ENGLISH_DICT.put("late", "leɪt");
        ENGLISH_DICT.put("young", "jʌŋ");
        ENGLISH_DICT.put("fast", "fɑːst");
        ENGLISH_DICT.put("slow", "sloʊ");
        ENGLISH_DICT.put("hard", "hɑːrd");
        ENGLISH_DICT.put("easy", "iːzi");
        ENGLISH_DICT.put("hot", "hɒt");
        ENGLISH_DICT.put("cold", "koʊld");
        ENGLISH_DICT.put("warm", "wɔːrm");
        ENGLISH_DICT.put("cool", "kuːl");
        ENGLISH_DICT.put("happy", "hæpi");
        ENGLISH_DICT.put("sad", "sæd");
        ENGLISH_DICT.put("angry", "æŋɡri");
        ENGLISH_DICT.put("tired", "taɪərd");
        ENGLISH_DICT.put("hungry", "hʌŋɡri");
        ENGLISH_DICT.put("thirsty", "θɜːrsti");
        ENGLISH_DICT.put("sick", "sɪk");
        ENGLISH_DICT.put("well", "wɛl");
        ENGLISH_DICT.put("time", "taɪm");
        ENGLISH_DICT.put("year", "jɪr");
        ENGLISH_DICT.put("people", "piːpl");
        ENGLISH_DICT.put("way", "weɪ");
        ENGLISH_DICT.put("day", "deɪ");
        ENGLISH_DICT.put("man", "mæn");
        ENGLISH_DICT.put("woman", "wʊmən");
        ENGLISH_DICT.put("child", "tʃaɪld");
        ENGLISH_DICT.put("children", "tʃɪldrən");
        ENGLISH_DICT.put("world", "wɜːld");
        ENGLISH_DICT.put("life", "laɪf");
        ENGLISH_DICT.put("hand", "hænd");
        ENGLISH_DICT.put("part", "pɑːrt");
        ENGLISH_DICT.put("place", "pleɪs");
        ENGLISH_DICT.put("case", "keɪs");
        ENGLISH_DICT.put("week", "wiːk");
        ENGLISH_DICT.put("company", "kʌmpəni");
        ENGLISH_DICT.put("system", "sɪstəm");
        ENGLISH_DICT.put("program", "proʊɡræm");
        ENGLISH_DICT.put("question", "kwɛstʃən");
        ENGLISH_DICT.put("work", "wɜːrk");
        ENGLISH_DICT.put("government", "ɡʌvərnmənt");
        ENGLISH_DICT.put("number", "nʌmbər");
        ENGLISH_DICT.put("night", "naɪt");
        ENGLISH_DICT.put("point", "pɔɪnt");
        ENGLISH_DICT.put("home", "hoʊm");
        ENGLISH_DICT.put("water", "wɔːtər");
        ENGLISH_DICT.put("room", "ruːm");
        ENGLISH_DICT.put("mother", "mʌðər");
        ENGLISH_DICT.put("area", "ɛriə");
        ENGLISH_DICT.put("money", "mʌni");
        ENGLISH_DICT.put("story", "stɔːri");
        ENGLISH_DICT.put("fact", "fækt");
        ENGLISH_DICT.put("month", "mʌnθ");
        ENGLISH_DICT.put("lot", "lɒt");
        ENGLISH_DICT.put("right", "raɪt");
        ENGLISH_DICT.put("study", "stʌdi");
        ENGLISH_DICT.put("book", "bʊk");
        ENGLISH_DICT.put("eye", "aɪ");
        ENGLISH_DICT.put("job", "dʒɒb");
        ENGLISH_DICT.put("word", "wɜːrd");
        ENGLISH_DICT.put("business", "bɪznɪs");
        ENGLISH_DICT.put("issue", "ɪʃuː");
        ENGLISH_DICT.put("side", "saɪd");
        ENGLISH_DICT.put("kind", "kaɪnd");
        ENGLISH_DICT.put("head", "hɛd");
        ENGLISH_DICT.put("house", "haʊs");
        ENGLISH_DICT.put("service", "sɜːrvɪs");
        ENGLISH_DICT.put("friend", "frɛnd");
        ENGLISH_DICT.put("father", "fɑːðər");
        ENGLISH_DICT.put("power", "paʊər");
        ENGLISH_DICT.put("hour", "aʊər");
        ENGLISH_DICT.put("game", "ɡeɪm");
        ENGLISH_DICT.put("line", "laɪn");
        ENGLISH_DICT.put("end", "ɛnd");
        ENGLISH_DICT.put("member", "mɛmbər");
        ENGLISH_DICT.put("law", "lɔː");
        ENGLISH_DICT.put("car", "kɑːr");
        ENGLISH_DICT.put("city", "sɪti");
        ENGLISH_DICT.put("community", "kəmjuːnɪti");
        ENGLISH_DICT.put("name", "neɪm");
        ENGLISH_DICT.put("president", "prɛzɪdənt");
        ENGLISH_DICT.put("team", "tiːm");
        ENGLISH_DICT.put("minute", "mɪnɪt");
        ENGLISH_DICT.put("idea", "aɪdiːə");
        ENGLISH_DICT.put("kid", "kɪd");
        ENGLISH_DICT.put("body", "bɒdi");
        ENGLISH_DICT.put("information", "ɪnfərmeɪʃən");
        ENGLISH_DICT.put("back", "bæk");
        ENGLISH_DICT.put("parent", "pɛrənt");
        ENGLISH_DICT.put("face", "feɪs");
        ENGLISH_DICT.put("others", "ʌðərz");
        ENGLISH_DICT.put("level", "lɛvəl");
        ENGLISH_DICT.put("office", "ɒfɪs");
        ENGLISH_DICT.put("door", "dɔːr");
        ENGLISH_DICT.put("health", "hɛlθ");
        ENGLISH_DICT.put("person", "pɜːrsən");
        ENGLISH_DICT.put("art", "ɑːrt");
        ENGLISH_DICT.put("war", "wɔːr");
        ENGLISH_DICT.put("history", "hɪstəri");
        ENGLISH_DICT.put("party", "pɑːrti");
        ENGLISH_DICT.put("result", "rɪzʌlt");
        ENGLISH_DICT.put("change", "tʃeɪndʒ");
        ENGLISH_DICT.put("morning", "mɔːrnɪŋ");
        ENGLISH_DICT.put("reason", "riːzən");
        ENGLISH_DICT.put("research", "riːsɜːrtʃ");
        ENGLISH_DICT.put("girl", "ɡɜːrl");
        ENGLISH_DICT.put("guy", "ɡaɪ");
        ENGLISH_DICT.put("moment", "moʊmənt");
        ENGLISH_DICT.put("air", "ɛr");
        ENGLISH_DICT.put("teacher", "tiːtʃər");
        ENGLISH_DICT.put("force", "fɔːrs");
        ENGLISH_DICT.put("education", "ɛdʒʊkeɪʃən");
    }

    private static void initHindiDict() {
        // Common Hindi words with their IPA pronunciations
        HINDI_DICT.put("नमस्ते", "nəmʌsteː");
        HINDI_DICT.put("धन्यवाद", "dʱənjəʋɑːd");
        HINDI_DICT.put("हाँ", "ɦɑ̃ː");
        HINDI_DICT.put("नहीं", "nəɦĩː");
        HINDI_DICT.put("है", "hɛː");
        HINDI_DICT.put("हैं", "hɛ̃ː");
        HINDI_DICT.put("था", "t̪ʰɑː");
        HINDI_DICT.put("थे", "t̪ʰeː");
        HINDI_DICT.put("थी", "t̪ʰiː");
        HINDI_DICT.put("हूँ", "ɦũː");
        HINDI_DICT.put("हो", "ɦoː");
        HINDI_DICT.put("होगा", "ɦoːɡɑː");
        HINDI_DICT.put("होगी", "ɦoːɡiː");
        HINDI_DICT.put("होगे", "ɦoːɡeː");
        HINDI_DICT.put("कर", "kər");
        HINDI_DICT.put("करना", "kərnɑː");
        HINDI_DICT.put("किया", "kɪjɑː");
        HINDI_DICT.put("कैसे", "kɛːseː");
        HINDI_DICT.put("क्या", "kjɑː");
        HINDI_DICT.put("कब", "kəb");
        HINDI_DICT.put("कहाँ", "kəɦɑ̃ː");
        HINDI_DICT.put("क्यों", "kjõː");
        HINDI_DICT.put("कौन", "kɔːn");
        HINDI_DICT.put("मैं", "mɛ̃ː");
        HINDI_DICT.put("तुम", "t̪ʊm");
        HINDI_DICT.put("आप", "ɑːp");
        HINDI_DICT.put("वह", "ʋəɦ");
        HINDI_DICT.put("वे", "ʋeː");
        HINDI_DICT.put("यह", "jəɦ");
        HINDI_DICT.put("ये", "jeː");
        HINDI_DICT.put("हम", "ɦəm");
        HINDI_DICT.put("उसका", "ʊskɑː");
        HINDI_DICT.put("उसकी", "ʊskiː");
        HINDI_DICT.put("उसके", "ʊskeː");
        HINDI_DICT.put("इसका", "ɪskɑː");
        HINDI_DICT.put("इसकी", "ɪskiː");
        HINDI_DICT.put("इसके", "ɪskeː");
        HINDI_DICT.put("मेरा", "meːrɑː");
        HINDI_DICT.put("मेरी", "meːriː");
        HINDI_DICT.put("मेरे", "meːreː");
        HINDI_DICT.put("तुम्हारा", "t̪ʊmɦɑːrɑː");
        HINDI_DICT.put("तुम्हारी", "t̪ʊmɦɑːriː");
        HINDI_DICT.put("तुम्हारे", "t̪ʊmɦɑːreː");
        HINDI_DICT.put("आपका", "ɑːpkɑː");
        HINDI_DICT.put("आपकी", "ɑːpkiː");
        HINDI_DICT.put("आपके", "ɑːpkeː");
        HINDI_DICT.put("और", "ɔːr");
        HINDI_DICT.put("या", "jɑː");
        HINDI_DICT.put("लेकिन", "lekɪn");
        HINDI_DICT.put("कि", "kɪ");
        HINDI_DICT.put("अगर", "əɡər");
        HINDI_DICT.put("तो", "t̪oː");
        HINDI_DICT.put("भी", "bʱiː");
        HINDI_DICT.put("ही", "ɦiː");
        HINDI_DICT.put("से", "seː");
        HINDI_DICT.put("में", "mẽː");
        HINDI_DICT.put("को", "koː");
        HINDI_DICT.put("पर", "pər");
        HINDI_DICT.put("तक", "t̪ək");
        HINDI_DICT.put("के", "keː");
        HINDI_DICT.put("का", "kɑː");
        HINDI_DICT.put("की", "kiː");
        HINDI_DICT.put("ने", "neː");
        HINDI_DICT.put("को", "koː");
        HINDI_DICT.put("से", "seː");
        HINDI_DICT.put("में", "mẽː");
        HINDI_DICT.put("को", "koː");
        HINDI_DICT.put("अच्छा", "ətʃːʰɑː");
        HINDI_DICT.put("बुरा", "bʊrɑː");
        HINDI_DICT.put("बड़ा", "bəɽɑː");
        HINDI_DICT.put("छोटा", "tʃʰoːʈɑː");
        HINDI_DICT.put("नया", "nəjɑː");
        HINDI_DICT.put("पुराना", "pʊrɑːnɑː");
        HINDI_DICT.put("लंबा", "ləmbɑː");
        HINDI_DICT.put("छोटा", "tʃʰoːʈɑː");
        HINDI_DICT.put("ऊँचा", "ũːtʃɑː");
        HINDI_DICT.put("नीचा", "niːtʃɑː");
        HINDI_DICT.put("जल्दी", "dʒəldiː");
        HINDI_DICT.put("धीरे", "dʱiːreː");
        HINDI_DICT.put("कठिन", "kəʈʰɪn");
        HINDI_DICT.put("आसान", "ɑːsɑːn");
        HINDI_DICT.put("गर्म", "ɡərəm");
        HINDI_DICT.put("ठंडा", "ʈʰəɳɖɑː");
        HINDI_DICT.put("खुश", "kʊʃ");
        HINDI_DICT.put("दुखी", "dʊkʰiː");
        HINDI_DICT.put("थका", "t̪ʰəkɑː");
        HINDI_DICT.put("भूखा", "bʱuːkʰɑː");
        HINDI_DICT.put("प्यासा", "pjɑːsɑː");
        HINDI_DICT.put("बीमार", "biːmɑːr");
        HINDI_DICT.put("स्वस्थ", "sʋəst̪ʰ");
        HINDI_DICT.put("समय", "səmej");
        HINDI_DICT.put("साल", "sɑːl");
        HINDI_DICT.put("लोग", "loːɡ");
        HINDI_DICT.put("रास्ता", "rɑːst̪ɑː");
        HINDI_DICT.put("दिन", "d̪ɪn");
        HINDI_DICT.put("आदमी", "ɑːd̪əmiː");
        HINDI_DICT.put("औरत", "ɔːrət̪");
        HINDI_DICT.put("बच्चा", "bətʃːʰɑː");
        HINDI_DICT.put("बच्चे", "bətʃːʰeː");
        HINDI_DICT.put("दुनिया", "d̪ʊnɪjɑː");
        HINDI_DICT.put("ज़िंदगी", "zɪnd̪ɡiː");
        HINDI_DICT.put("हाथ", "ɦɑːt̪ʰ");
        HINDI_DICT.put("हिस्सा", "ɦɪssɑː");
        HINDI_DICT.put("जगह", "dʒəɡəɦ");
        HINDI_DICT.put("मामला", "mɑːməlɑː");
        HINDI_DICT.put("हफ्ता", "ɦəpʰt̪ɑː");
        HINDI_DICT.put("कंपनी", "kəmpəniː");
        HINDI_DICT.put("प्रणाली", "prəɳɑːliː");
        HINDI_DICT.put("प्रोग्राम", "proːɡrɑːm");
        HINDI_DICT.put("सवाल", "səʋɑːl");
        HINDI_DICT.put("काम", "kɑːm");
        HINDI_DICT.put("सरकार", "sərkɑːr");
        HINDI_DICT.put("संख्या", "səmkʰjɑː");
        HINDI_DICT.put("रात", "rɑːt̪");
        HINDI_DICT.put("बिंदु", "bɪnd̪ʊ");
        HINDI_DICT.put("घर", "ɡʱər");
        HINDI_DICT.put("पानी", "pɑːniː");
        HINDI_DICT.put("कमरा", "kəmraː");
        HINDI_DICT.put("माँ", "mɑ̃ː");
        HINDI_DICT.put("क्षेत्र", "kʃet̪rə");
        HINDI_DICT.put("पैसा", "pɛːsɑː");
        HINDI_DICT.put("कहानी", "kəɦɑːniː");
        HINDI_DICT.put("तथ्य", "t̪ətʰj");
        HINDI_DICT.put("महीना", "məɦiːnɑː");
        HINDI_DICT.put("बहुत", "bəɦʊt̪");
        HINDI_DICT.put("सही", "səɦiː");
        HINDI_DICT.put("अध्ययन", "əd̪ʱjən");
        HINDI_DICT.put("किताब", "kɪt̪ɑːb");
        HINDI_DICT.put("आँख", "ɑ̃ːkʰ");
        HINDI_DICT.put("नौकरी", "nɔːkəriː");
        HINDI_DICT.put("शब्द", "ʃəbd̪");
        HINDI_DICT.put("व्यापार", "vjɑːpɑːr");
        HINDI_DICT.put("मुद्दा", "mʊd̪ːɑː");
        HINDI_DICT.put("पक्ष", "pəkʃ");
        HINDI_DICT.put("प्रकार", "prəkɑːr");
        HINDI_DICT.put("सिर", "sɪr");
        HINDI_DICT.put("मकान", "məkɑːn");
        HINDI_DICT.put("सेवा", "seʋɑː");
        HINDI_DICT.put("दोस्त", "d̪oːst̪");
        HINDI_DICT.put("पिता", "pɪt̪ɑː");
        HINDI_DICT.put("शक्ति", "ʃəkt̪iː");
        HINDI_DICT.put("घंटा", "ɡʱəɳʈɑː");
        HINDI_DICT.put("खेल", "kʰeːl");
        HINDI_DICT.put("रेखा", "rekʰɑː");
        HINDI_DICT.put("अंत", "ənt̪");
        HINDI_DICT.put("सदस्य", "səd̪əsj");
        HINDI_DICT.put("कानून", "kɑːnʊn");
        HINDI_DICT.put("कार", "kɑːr");
        HINDI_DICT.put("शहर", "ʃəɦər");
        HINDI_DICT.put("समुदाय", "səmʊd̪ɑːj");
        HINDI_DICT.put("नाम", "nɑːm");
        HINDI_DICT.put("राष्ट्रपति", "rɑːʂʈrəpət̪iː");
        HINDI_DICT.put("टीम", "ʈiːm");
        HINDI_DICT.put("मिनट", "mɪnɪʈ");
        HINDI_DICT.put("विचार", "ʋɪtʃɑːr");
        HINDI_DICT.put("बच्चा", "bətʃːʰɑː");
        HINDI_DICT.put("शरीर", "ʃəriːr");
        HINDI_DICT.put("सूचना", "sʊtʃənɑː");
        HINDI_DICT.put("पीठ", "piːʈʰ");
        HINDI_DICT.put("माता-पिता", "mɑːt̪ɑːpɪt̪ɑː");
        HINDI_DICT.put("चेहरा", "tʃʰəɦrɑː");
    }

    // JNI method (kept for compatibility, but now we use pure Java)
    public static native String nativePhonemize(String text, String lang);
}
