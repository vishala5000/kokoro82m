package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;

public class Phonemizer {

    private static final Map<String, String> EN_DICT = new HashMap<>();
    private static final Map<String, String> HI_DICT = new HashMap<>();
    private static final Map<String, String> NUM_EN = new HashMap<>();
    private static final Map<String, String> NUM_HI = new HashMap<>();

    static {
        // ============ ENGLISH DICTIONARY ============
        EN_DICT.put("i", "aɪ"); EN_DICT.put("me", "mi"); EN_DICT.put("my", "maɪ");
        EN_DICT.put("you", "ju"); EN_DICT.put("your", "jɔɹ");
        EN_DICT.put("he", "hi"); EN_DICT.put("him", "hɪm"); EN_DICT.put("his", "hɪz");
        EN_DICT.put("she", "ʃi"); EN_DICT.put("her", "hɜɹ");
        EN_DICT.put("it", "ɪt"); EN_DICT.put("we", "wi"); EN_DICT.put("us", "ʌs");
        EN_DICT.put("they", "ðeɪ"); EN_DICT.put("them", "ðɛm"); EN_DICT.put("their", "ðɛɹ");
        EN_DICT.put("this", "ðɪs"); EN_DICT.put("that", "ðæt");
        EN_DICT.put("these", "ðiːz"); EN_DICT.put("those", "ðoʊz");
        EN_DICT.put("who", "hu"); EN_DICT.put("what", "wɒt"); EN_DICT.put("which", "wɪʧ");
        EN_DICT.put("where", "wɛɹ"); EN_DICT.put("when", "wɛn");
        EN_DICT.put("why", "waɪ"); EN_DICT.put("how", "haʊ");

        EN_DICT.put("am", "æm"); EN_DICT.put("is", "ɪz"); EN_DICT.put("are", "ɑɹ");
        EN_DICT.put("was", "wɒz"); EN_DICT.put("were", "wɜɹ");
        EN_DICT.put("be", "bi"); EN_DICT.put("been", "bɪn");
        EN_DICT.put("have", "hæv"); EN_DICT.put("has", "hæz"); EN_DICT.put("had", "hæd");
        EN_DICT.put("do", "du"); EN_DICT.put("does", "dʌz"); EN_DICT.put("did", "dɪd");
        EN_DICT.put("will", "wɪl"); EN_DICT.put("would", "wʊd");
        EN_DICT.put("can", "kæn"); EN_DICT.put("could", "kʊd");
        EN_DICT.put("shall", "ʃæl"); EN_DICT.put("should", "ʃʊd");
        EN_DICT.put("may", "meɪ"); EN_DICT.put("might", "maɪt");
        EN_DICT.put("must", "mʌst");
        EN_DICT.put("go", "ɡoʊ"); EN_DICT.put("went", "wɛnt"); EN_DICT.put("gone", "ɡɒn");
        EN_DICT.put("come", "kʌm"); EN_DICT.put("came", "keɪm");
        EN_DICT.put("see", "siː"); EN_DICT.put("saw", "sɔː"); EN_DICT.put("seen", "siːn");
        EN_DICT.put("know", "noʊ"); EN_DICT.put("knew", "njuː"); EN_DICT.put("known", "noʊn");
        EN_DICT.put("think", "θɪŋk"); EN_DICT.put("thought", "θɔːt");
        EN_DICT.put("make", "meɪk"); EN_DICT.put("made", "meɪd");
        EN_DICT.put("take", "teɪk"); EN_DICT.put("took", "tʊk"); EN_DICT.put("taken", "teɪkən");
        EN_DICT.put("give", "ɡɪv"); EN_DICT.put("gave", "ɡeɪv"); EN_DICT.put("given", "ɡɪvən");
        EN_DICT.put("say", "seɪ"); EN_DICT.put("says", "sɛz"); EN_DICT.put("said", "sɛd");
        EN_DICT.put("tell", "tɛl"); EN_DICT.put("told", "toʊld");
        EN_DICT.put("get", "ɡɛt"); EN_DICT.put("got", "ɡɒt");
        EN_DICT.put("want", "wɒnt"); EN_DICT.put("need", "niːd");
        EN_DICT.put("like", "laɪk"); EN_DICT.put("love", "lʌv");
        EN_DICT.put("feel", "fiːl"); EN_DICT.put("felt", "fɛlt");
        EN_DICT.put("try", "tɹaɪ"); EN_DICT.put("tried", "tɹaɪd");
        EN_DICT.put("use", "juːz"); EN_DICT.put("used", "juːzd");
        EN_DICT.put("find", "faɪnd"); EN_DICT.put("found", "faʊnd");
        EN_DICT.put("work", "wɜɹk"); EN_DICT.put("worked", "wɜɹkt");
        EN_DICT.put("call", "kɔːl"); EN_DICT.put("called", "kɔːld");
        EN_DICT.put("help", "hɛlp"); EN_DICT.put("play", "pleɪ");
        EN_DICT.put("run", "ɹʌn"); EN_DICT.put("ran", "ɹæn");
        EN_DICT.put("walk", "wɔːk"); EN_DICT.put("talk", "tɔːk");
        EN_DICT.put("ask", "æsk"); EN_DICT.put("eat", "iːt"); EN_DICT.put("ate", "eɪt");
        EN_DICT.put("drink", "dɹɪŋk"); EN_DICT.put("read", "ɹiːd");
        EN_DICT.put("write", "ɹaɪt"); EN_DICT.put("wrote", "ɹoʊt");
        EN_DICT.put("learn", "lɜɹn"); EN_DICT.put("speak", "spiːk");
        EN_DICT.put("listen", "lɪsən"); EN_DICT.put("look", "lʊk");
        EN_DICT.put("watch", "wɒʧ"); EN_DICT.put("open", "oʊpən");
        EN_DICT.put("close", "kloʊz"); EN_DICT.put("start", "stɑɹt");
        EN_DICT.put("stop", "stɒp"); EN_DICT.put("wait", "weɪt");
        EN_DICT.put("move", "muːv"); EN_DICT.put("live", "lɪv");
        EN_DICT.put("buy", "baɪ"); EN_DICT.put("bought", "bɔːt");
        EN_DICT.put("send", "sɛnd"); EN_DICT.put("sent", "sɛnt");
        EN_DICT.put("keep", "kiːp"); EN_DICT.put("kept", "kɛpt");
        EN_DICT.put("let", "lɛt"); EN_DICT.put("leave", "liːv"); EN_DICT.put("left", "lɛft");
        EN_DICT.put("begin", "bɪɡɪn"); EN_DICT.put("began", "bɪɡæn");
        EN_DICT.put("hold", "hoʊld"); EN_DICT.put("held", "hɛld");
        EN_DICT.put("bring", "bɹɪŋ"); EN_DICT.put("brought", "bɹɔːt");
        EN_DICT.put("happen", "hæpən"); EN_DICT.put("happened", "hæpənd");

        EN_DICT.put("the", "ðə"); EN_DICT.put("a", "ə"); EN_DICT.put("an", "æn");
        EN_DICT.put("and", "ænd"); EN_DICT.put("or", "ɔɹ"); EN_DICT.put("but", "bʌt");
        EN_DICT.put("if", "ɪf"); EN_DICT.put("then", "ðɛn");
        EN_DICT.put("so", "soʊ"); EN_DICT.put("as", "æz");
        EN_DICT.put("of", "ɒv"); EN_DICT.put("in", "ɪn"); EN_DICT.put("on", "ɒn");
        EN_DICT.put("at", "æt"); EN_DICT.put("to", "tu"); EN_DICT.put("for", "fɔɹ");
        EN_DICT.put("with", "wɪð"); EN_DICT.put("from", "fɹɒm");
        EN_DICT.put("by", "baɪ"); EN_DICT.put("about", "əbaʊt");
        EN_DICT.put("into", "ɪntu"); EN_DICT.put("through", "θɹu");
        EN_DICT.put("before", "bɪfɔɹ"); EN_DICT.put("after", "æftəɹ");
        EN_DICT.put("above", "əbʌv"); EN_DICT.put("below", "bɪloʊ");
        EN_DICT.put("between", "bɪtwiːn"); EN_DICT.put("under", "ʌndəɹ");
        EN_DICT.put("over", "oʊvəɹ"); EN_DICT.put("up", "ʌp"); EN_DICT.put("down", "daʊn");
        EN_DICT.put("out", "aʊt"); EN_DICT.put("off", "ɒf");
        EN_DICT.put("again", "əɡɛn"); EN_DICT.put("also", "ɔːlsoʊ");
        EN_DICT.put("very", "vɛɹi"); EN_DICT.put("just", "ʤʌst");
        EN_DICT.put("only", "oʊnli"); EN_DICT.put("still", "stɪl");
        EN_DICT.put("never", "nɛvəɹ"); EN_DICT.put("always", "ɔːlweɪz");
        EN_DICT.put("now", "naʊ"); EN_DICT.put("here", "hiːɹ"); EN_DICT.put("there", "ðɛɹ");
        EN_DICT.put("yes", "jɛs"); EN_DICT.put("no", "noʊ");
        EN_DICT.put("not", "nɒt"); EN_DICT.put("please", "pliːz");
        EN_DICT.put("thank", "θæŋk"); EN_DICT.put("thanks", "θæŋks");
        EN_DICT.put("sorry", "sɒɹi"); EN_DICT.put("welcome", "wɛlkəm");
        EN_DICT.put("hello", "hɛloʊ"); EN_DICT.put("hi", "haɪ");
        EN_DICT.put("goodbye", "ɡʊdbaɪ"); EN_DICT.put("bye", "baɪ");
        EN_DICT.put("okay", "oʊkeɪ"); EN_DICT.put("ok", "oʊkeɪ");
        EN_DICT.put("today", "tədeɪ"); EN_DICT.put("tomorrow", "təmɒɹoʊ");
        EN_DICT.put("yesterday", "jɛstəɹdeɪ");
        EN_DICT.put("morning", "mɔɹnɪŋ"); EN_DICT.put("night", "naɪt");
        EN_DICT.put("time", "taɪm"); EN_DICT.put("year", "jiːɹ");
        EN_DICT.put("day", "deɪ"); EN_DICT.put("week", "wiːk");
        EN_DICT.put("man", "mæn"); EN_DICT.put("woman", "wʊmən");
        EN_DICT.put("child", "ʧaɪld"); EN_DICT.put("children", "ʧɪldɹən");
        EN_DICT.put("boy", "bɔɪ"); EN_DICT.put("girl", "ɡɜɹl");
        EN_DICT.put("friend", "fɹɛnd"); EN_DICT.put("family", "fæməli");
        EN_DICT.put("mother", "mʌðəɹ"); EN_DICT.put("father", "fɑːðəɹ");
        EN_DICT.put("home", "hoʊm"); EN_DICT.put("house", "haʊs");
        EN_DICT.put("school", "skuːl"); EN_DICT.put("money", "mʌni");
        EN_DICT.put("food", "fuːd"); EN_DICT.put("water", "wɔːtəɹ");
        EN_DICT.put("book", "bʊk"); EN_DICT.put("car", "kɑɹ");
        EN_DICT.put("world", "wɜɹld"); EN_DICT.put("country", "kʌntɹi");
        EN_DICT.put("city", "sɪti"); EN_DICT.put("thing", "θɪŋ");
        EN_DICT.put("life", "laɪf"); EN_DICT.put("love", "lʌv");
        EN_DICT.put("heart", "hɑɹt"); EN_DICT.put("good", "ɡʊd");
        EN_DICT.put("bad", "bæd"); EN_DICT.put("great", "ɡɹeɪt");
        EN_DICT.put("big", "bɪɡ"); EN_DICT.put("small", "smɔːl");
        EN_DICT.put("new", "njuː"); EN_DICT.put("old", "oʊld");
        EN_DICT.put("hot", "hɒt"); EN_DICT.put("cold", "koʊld");
        EN_DICT.put("fast", "fæst"); EN_DICT.put("slow", "sloʊ");
        EN_DICT.put("happy", "hæpi"); EN_DICT.put("sad", "sæd");
        EN_DICT.put("one", "wʌn"); EN_DICT.put("two", "tuː");
        EN_DICT.put("three", "θɹiː"); EN_DICT.put("four", "fɔɹ");
        EN_DICT.put("five", "faɪv"); EN_DICT.put("six", "sɪks");
        EN_DICT.put("seven", "sɛvən"); EN_DICT.put("eight", "eɪt");
        EN_DICT.put("nine", "naɪn"); EN_DICT.put("ten", "tɛn");
        EN_DICT.put("test", "tɛst"); EN_DICT.put("speech", "spiʧ");
        EN_DICT.put("text", "tɛkst"); EN_DICT.put("voice", "vɔɪs");
        EN_DICT.put("kokoro", "koʊkoʊɹoʊ");

        // ============ HINDI DICTIONARY ============
        HI_DICT.put("नमस्ते", "nəmasteː");
        HI_DICT.put("नमस्कार", "nəmskɑːɹ");
        HI_DICT.put("धन्यवाद", "d̪ʱənjiəʋɑːd̪");
        HI_DICT.put("शुक्रिया", "ʃʊkɹiə");
        HI_DICT.put("हाँ", "ɦɑ̃ː"); HI_DICT.put("हां", "ɦɑ̃ː");
        HI_DICT.put("नहीं", "nəɦĩː"); HI_DICT.put("नही", "nəɦiː");
        HI_DICT.put("जी", "ʤiː");
        HI_DICT.put("अच्छा", "ətʃʰɑː");
        HI_DICT.put("ठीक", "t̪ʰiːk");
        HI_DICT.put("सही", "səɦiː");
        HI_DICT.put("गलत", "ɡələt̪");
        HI_DICT.put("मैं", "mɛ̃ː");
        HI_DICT.put("तू", "t̪uː"); HI_DICT.put("तुम", "t̪ʊm"); HI_DICT.put("आप", "ɑːp");
        HI_DICT.put("हम", "ɦəm");
        HI_DICT.put("वह", "ʋəɦ"); HI_DICT.put("वो", "ʋoː"); HI_DICT.put("वे", "ʋeː");
        HI_DICT.put("यह", "jəɦ"); HI_DICT.put("ये", "jeː");
        HI_DICT.put("इस", "ɪs"); HI_DICT.put("उस", "ʊs");
        HI_DICT.put("क्या", "kjɑː");
        HI_DICT.put("कैसे", "kɛːseː");
        HI_DICT.put("कब", "kəb");
        HI_DICT.put("कहाँ", "kəɦɑ̃ː"); HI_DICT.put("कहां", "kəɦɑ̃ː");
        HI_DICT.put("क्यों", "kjõː");
        HI_DICT.put("कौन", "kɔːn");
        HI_DICT.put("है", "hɛː"); HI_DICT.put("हैं", "hɛ̃ː");
        HI_DICT.put("था", "t̪ʰɑː"); HI_DICT.put("थी", "t̪ʰiː"); HI_DICT.put("थे", "t̪ʰeː");
        HI_DICT.put("हूँ", "ɦũː"); HI_DICT.put("हो", "ɦoː");
        HI_DICT.put("होगा", "ɦoːɡɑː"); HI_DICT.put("होगी", "ɦoːɡiː");
        HI_DICT.put("कर", "kəɹ"); HI_DICT.put("करना", "kəɹnɑː");
        HI_DICT.put("किया", "kiə");
        HI_DICT.put("जा", "ʤɑː"); HI_DICT.put("जाना", "ʤɑːnɑː");
        HI_DICT.put("गया", "ɡəjɑː"); HI_DICT.put("गई", "ɡəjiː");
        HI_DICT.put("आ", "ɑː"); HI_DICT.put("आना", "ɑːnɑː");
        HI_DICT.put("आया", "ɑːjɑː");
        HI_DICT.put("दे", "d̪eː"); HI_DICT.put("देना", "d̪eːnɑː");
        HI_DICT.put("दिया", "d̪iə");
        HI_DICT.put("ले", "leː"); HI_DICT.put("लेना", "leːnɑː");
        HI_DICT.put("लिया", "liə");
        HI_DICT.put("बोल", "boːl"); HI_DICT.put("बोलना", "boːlnɑː");
        HI_DICT.put("सुन", "sʊn"); HI_DICT.put("सुनना", "sʊnnɑː");
        HI_DICT.put("देख", "d̪eːkʰ"); HI_DICT.put("देखना", "d̪eːkʰnɑː");
        HI_DICT.put("खा", "kʰɑː"); HI_DICT.put("खाना", "kʰɑːnɑː");
        HI_DICT.put("पी", "piː"); HI_DICT.put("पीना", "piːnɑː");
        HI_DICT.put("भारत", "bʱɑːɹət̪");
        HI_DICT.put("हिंदी", "ɦind̪iː"); HI_DICT.put("हिन्दी", "ɦind̪iː");
        HI_DICT.put("दुनिया", "d̪ʊniə");
        HI_DICT.put("ज़िंदगी", "zind̪ɡiː");
        HI_DICT.put("प्यार", "pjɑːɹ");
        HI_DICT.put("दोस्त", "d̪oːst̪");
        HI_DICT.put("परिवार", "pəɹiʋɑːɹ");
        HI_DICT.put("घर", "ɡʱəɹ");
        HI_DICT.put("पानी", "pɑːniː");
        HI_DICT.put("रोटी", "ɹoːʈiː");
        HI_DICT.put("चाय", "ʧɑːeː");
        HI_DICT.put("किताब", "kit̪ɑːb");
        HI_DICT.put("काम", "kɑːm");
        HI_DICT.put("पैसा", "pɛːsɑː");
        HI_DICT.put("समय", "səməeː");
        HI_DICT.put("दिन", "d̪in");
        HI_DICT.put("रात", "ɹɑːt̪");
        HI_DICT.put("सुबह", "sʊbəɦ");
        HI_DICT.put("शाम", "ʃɑːm");
        HI_DICT.put("आज", "ɑːʤ");
        HI_DICT.put("कल", "kəl");
        HI_DICT.put("एक", "eːk"); HI_DICT.put("दो", "d̪oː");
        HI_DICT.put("तीन", "t̪iːn"); HI_DICT.put("चार", "ʧɑːɹ");
        HI_DICT.put("पांच", "pɑːnʧ"); HI_DICT.put("सात", "sɑːt̪");
        HI_DICT.put("आठ", "ɑːʈʰ"); HI_DICT.put("नौ", "nɔː");
        HI_DICT.put("दस", "d̪əs");

        // Numbers
        NUM_EN.put("0", "ziːɹoʊ"); NUM_EN.put("1", "wʌn");
        NUM_EN.put("2", "tuː"); NUM_EN.put("3", "θɹiː");
        NUM_EN.put("4", "fɔɹ"); NUM_EN.put("5", "faɪv");
        NUM_EN.put("6", "sɪks"); NUM_EN.put("7", "sɛvən");
        NUM_EN.put("8", "eɪt"); NUM_EN.put("9", "naɪn");

        NUM_HI.put("०", "ʃʊnjiə"); NUM_HI.put("१", "eːk");
        NUM_HI.put("२", "d̪oː"); NUM_HI.put("३", "t̪iːn");
        NUM_HI.put("४", "ʧɑːɹ"); NUM_HI.put("५", "pɑːnʧ");
        NUM_HI.put("६", "ʧʰəɦ"); NUM_HI.put("७", "sɑːt̪");
        NUM_HI.put("८", "ɑːʈʰ"); NUM_HI.put("९", "nɔː");
    }

    /**
     * Auto-detects language from voice ID prefix:
     *   - "am_", "bf_", "bf_", "ef_", "af_" → English
     *   - "hm_", "hf_" → Hindi
     *   - "jm_", "jf_" → Japanese
     *   - etc.
     */
    public static String phonemize(String text, String voiceId) {
        if (text == null || text.trim().isEmpty()) return "";
        text = text.trim();

        // Auto-detect language from voice ID
        if (voiceId.startsWith("am_") || voiceId.startsWith("bf_") ||
            voiceId.startsWith("bm_") || voiceId.startsWith("af_") ||
            voiceId.startsWith("ef_") || voiceId.startsWith("em_")) {
            return phonemizeEnglish(text);
        } else if (voiceId.startsWith("hm_") || voiceId.startsWith("hf_")) {
            return phonemizeHindi(text);
        } else {
            // Default to English for unknown voices
            return phonemizeEnglish(text);
        }
    }

    private static String phonemizeEnglish(String text) {
        text = text.toLowerCase();
        text = text.replace(",", " , ").replace(".", " . ");
        text = text.replace("!", " ! ").replace("?", " ? ");
        text = text.replace(";", " ; ").replace(":", " : ");
        text = text.replace("-", " ");
        text = text.replaceAll("\\s+", " ").trim();

        StringBuilder result = new StringBuilder();
        String[] tokens = text.split("\\s+");
        boolean first = true;

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (",.!?;:".contains(token)) {
                result.append(token);
                first = false;
                continue;
            }

            String clean = token.replaceAll("[^a-z0-9']", "");
            if (clean.isEmpty()) continue;

            String ipa = EN_DICT.get(clean);
            if (ipa == null && clean.matches("\\d+")) {
                ipa = numberToIpaEn(clean);
            }
            if (ipa == null) {
                ipa = englishFallback(clean);
            }

            if (!first) result.append(" ");
            result.append(ipa);
            first = false;
        }
        return result.toString();
    }

    private static String numberToIpaEn(String num) {
        if (NUM_EN.containsKey(num)) return NUM_EN.get(num);
        StringBuilder sb = new StringBuilder();
        for (char c : num.toCharArray()) {
            String d = NUM_EN.get(String.valueOf(c));
            if (d != null) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(d);
            }
        }
        return sb.toString();
    }

    private static String englishFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char next = (i + 1 < word.length()) ? word.charAt(i + 1) : ' ';

            if (c == 'a') ipa.append(next == 'i' ? "eɪ" : (next == 'u' ? "ɔː" : "æ"));
            else if (c == 'e') ipa.append((next == 'a' || next == 'i') ? "iː" : (i == word.length() - 1 ? "" : "ɛ"));
            else if (c == 'i') ipa.append(next == 'e' ? "aɪ" : "ɪ");
            else if (c == 'o') ipa.append(next == 'e' ? "oʊ" : "ɒ");
            else if (c == 'u') ipa.append(next == 'e' ? "juː" : "ʌ");
            else if (c == 'g') ipa.append("ɡ");
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
        text = text.replace(",", " , ").replace(".", " . ");
        text = text.replace("!", " ! ").replace("?", " ? ");
        text = text.replace(";", " ; ").replace(":", " : ");
        text = text.replaceAll("\\s+", " ").trim();

        StringBuilder result = new StringBuilder();
        String[] tokens = text.split("\\s+");
        boolean first = true;

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (",.!?;:".contains(token)) {
                result.append(token);
                first = false;
                continue;
            }

            String ipa = HI_DICT.get(token);
            if (ipa == null && token.matches("[०-९]+")) {
                ipa = numberToIpaHi(token);
            }
            if (ipa == null && token.matches("\\d+")) {
                ipa = numberToIpaEn(token);
            }
            if (ipa == null) {
                ipa = hindiFallback(token);
            }

            if (!first) result.append(" ");
            result.append(ipa);
            first = false;
        }
        return result.toString();
    }

    private static String numberToIpaHi(String num) {
        StringBuilder sb = new StringBuilder();
        for (char c : num.toCharArray()) {
            String d = NUM_HI.get(String.valueOf(c));
            if (d != null) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(d);
            }
        }
        return sb.toString();
    }

    private static String hindiFallback(String word) {
        StringBuilder ipa = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
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
            else if (c == 'क') ipa.append("kə");
            else if (c == 'ख') ipa.append("kʰə");
            else if (c == 'ग') ipa.append("ɡə");
            else if (c == 'घ') ipa.append("ɡʱə");
            else if (c == 'च') ipa.append("ʧə");
            else if (c == 'छ') ipa.append("ʧʰə");
            else if (c == 'ज') ipa.append("ʤə");
            else if (c == 'झ') ipa.append("ʤʱə");
            else if (c == 'ट') ipa.append("ʈə");
            else if (c == 'ठ') ipa.append("ʈʰə");
            else if (c == 'ड') ipa.append("ɖə");
            else if (c == 'ढ') ipa.append("ɖʱə");
            else if (c == 'त') ipa.append("t̪ə");
            else if (c == 'थ') ipa.append("t̪ʰə");
            else if (c == 'द') ipa.append("d̪ə");
            else if (c == 'ध') ipa.append("d̪ʱə");
            else if (c == 'न') ipa.append("nə");
            else if (c == 'प') ipa.append("pə");
            else if (c == 'फ') ipa.append("pʰə");
            else if (c == 'ब') ipa.append("bə");
            else if (c == 'भ') ipa.append("bʱə");
            else if (c == 'म') ipa.append("mə");
            else if (c == 'य') ipa.append("jə");
            else if (c == 'र') ipa.append("ɾə");
            else if (c == 'ल') ipa.append("lə");
            else if (c == 'व') ipa.append("ʋə");
            else if (c == 'श') ipa.append("ʃə");
            else if (c == 'स') ipa.append("sə");
            else if (c == 'ह') ipa.append("ɦə");
            else if (c == '्') {
                int len = ipa.length();
                if (len >= 1 && ipa.substring(len - 1).equals("ə")) {
                    ipa.delete(len - 1, len);
                }
            }
            else if (c == 'ं' || c == 'ँ') ipa.append("̃");
            else if (c == 'ः') ipa.append("h");
            else if (c >= '०' && c <= '९') {
                String d = NUM_HI.get(String.valueOf(c));
                if (d != null) ipa.append(d);
            }
        }
        return ipa.toString();
    }
}
