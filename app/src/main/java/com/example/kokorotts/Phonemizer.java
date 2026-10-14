package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;

public class Phonemizer {

    private static final Map<String, String> EN_DICT = new HashMap<>();
    private static final Map<String, String> HI_DICT = new HashMap<>();
    private static final Map<String, String> NUM_EN = new HashMap<>();
    private static final Map<String, String> NUM_HI = new HashMap<>();

    static {
        // ============ ENGLISH DICTIONARY (300+ words) ============
        // Pronouns & basic
        EN_DICT.put("i", "aɪ"); EN_DICT.put("me", "mi"); EN_DICT.put("my", "maɪ");
        EN_DICT.put("mine", "maɪn"); EN_DICT.put("myself", "maɪsɛlf");
        EN_DICT.put("you", "ju"); EN_DICT.put("your", "jɔɹ"); EN_DICT.put("yours", "jɔɹz");
        EN_DICT.put("yourself", "jɔɹsɛlf");
        EN_DICT.put("he", "hi"); EN_DICT.put("him", "hɪm"); EN_DICT.put("his", "hɪz");
        EN_DICT.put("she", "ʃi"); EN_DICT.put("her", "hɜɹ"); EN_DICT.put("hers", "hɜɹz");
        EN_DICT.put("it", "ɪt"); EN_DICT.put("its", "ɪts");
        EN_DICT.put("we", "wi"); EN_DICT.put("us", "ʌs"); EN_DICT.put("our", "aʊɹ");
        EN_DICT.put("ours", "aʊɹz");
        EN_DICT.put("they", "ðeɪ"); EN_DICT.put("them", "ðɛm"); EN_DICT.put("their", "ðɛɹ");
        EN_DICT.put("theirs", "ðɛɹz");
        EN_DICT.put("this", "ðɪs"); EN_DICT.put("that", "ðæt");
        EN_DICT.put("these", "ðiːz"); EN_DICT.put("those", "ðoʊz");
        EN_DICT.put("who", "hu"); EN_DICT.put("whom", "hum"); EN_DICT.put("whose", "huz");
        EN_DICT.put("what", "wɒt"); EN_DICT.put("which", "wɪʧ");
        EN_DICT.put("where", "wɛɹ"); EN_DICT.put("when", "wɛn");
        EN_DICT.put("why", "waɪ"); EN_DICT.put("how", "haʊ");

        // Verbs (common)
        EN_DICT.put("am", "æm"); EN_DICT.put("is", "ɪz"); EN_DICT.put("are", "ɑɹ");
        EN_DICT.put("was", "wɒz"); EN_DICT.put("were", "wɜɹ");
        EN_DICT.put("be", "bi"); EN_DICT.put("been", "bɪn"); EN_DICT.put("being", "biːɪŋ");
        EN_DICT.put("have", "hæv"); EN_DICT.put("has", "hæz"); EN_DICT.put("had", "hæd");
        EN_DICT.put("do", "du"); EN_DICT.put("does", "dʌz"); EN_DICT.put("did", "dɪd");
        EN_DICT.put("done", "dʌn"); EN_DICT.put("doing", "duːɪŋ");
        EN_DICT.put("will", "wɪl"); EN_DICT.put("would", "wʊd");
        EN_DICT.put("can", "kæn"); EN_DICT.put("could", "kʊd");
        EN_DICT.put("shall", "ʃæl"); EN_DICT.put("should", "ʃʊd");
        EN_DICT.put("may", "meɪ"); EN_DICT.put("might", "maɪt");
        EN_DICT.put("must", "mʌst");
        EN_DICT.put("go", "ɡoʊ"); EN_DICT.put("goes", "ɡoʊz");
        EN_DICT.put("went", "wɛnt"); EN_DICT.put("gone", "ɡɒn"); EN_DICT.put("going", "ɡoʊɪŋ");
        EN_DICT.put("come", "kʌm"); EN_DICT.put("comes", "kʌmz");
        EN_DICT.put("came", "keɪm"); EN_DICT.put("coming", "kʌmɪŋ");
        EN_DICT.put("see", "siː"); EN_DICT.put("sees", "siːz");
        EN_DICT.put("saw", "sɔː"); EN_DICT.put("seen", "siːn"); EN_DICT.put("seeing", "siːɪŋ");
        EN_DICT.put("know", "noʊ"); EN_DICT.put("knows", "noʊz");
        EN_DICT.put("knew", "njuː"); EN_DICT.put("known", "noʊn"); EN_DICT.put("knowing", "noʊɪŋ");
        EN_DICT.put("think", "θɪŋk"); EN_DICT.put("thinks", "θɪŋks");
        EN_DICT.put("thought", "θɔːt"); EN_DICT.put("thinking", "θɪŋkɪŋ");
        EN_DICT.put("make", "meɪk"); EN_DICT.put("makes", "meɪks");
        EN_DICT.put("made", "meɪd"); EN_DICT.put("making", "meɪkɪŋ");
        EN_DICT.put("take", "teɪk"); EN_DICT.put("takes", "teɪks");
        EN_DICT.put("took", "tʊk"); EN_DICT.put("taken", "teɪkən"); EN_DICT.put("taking", "teɪkɪŋ");
        EN_DICT.put("give", "ɡɪv"); EN_DICT.put("gives", "ɡɪvz");
        EN_DICT.put("gave", "ɡeɪv"); EN_DICT.put("given", "ɡɪvən"); EN_DICT.put("giving", "ɡɪvɪŋ");
        EN_DICT.put("say", "seɪ"); EN_DICT.put("says", "sɛz");
        EN_DICT.put("said", "sɛd"); EN_DICT.put("saying", "seɪɪŋ");
        EN_DICT.put("tell", "tɛl"); EN_DICT.put("tells", "tɛlz");
        EN_DICT.put("told", "toʊld"); EN_DICT.put("telling", "tɛlɪŋ");
        EN_DICT.put("get", "ɡɛt"); EN_DICT.put("gets", "ɡɛts");
        EN_DICT.put("got", "ɡɒt"); EN_DICT.put("gotten", "ɡɒtən");
        EN_DICT.put("want", "wɒnt"); EN_DICT.put("wants", "wɒnts"); EN_DICT.put("wanted", "wɒntɪd");
        EN_DICT.put("need", "niːd"); EN_DICT.put("needs", "niːdz"); EN_DICT.put("needed", "niːdɪd");
        EN_DICT.put("like", "laɪk"); EN_DICT.put("likes", "laɪks"); EN_DICT.put("liked", "laɪkt");
        EN_DICT.put("love", "lʌv"); EN_DICT.put("loves", "lʌvz"); EN_DICT.put("loved", "lʌvd");
        EN_DICT.put("hate", "heɪt"); EN_DICT.put("hates", "heɪts");
        EN_DICT.put("feel", "fiːl"); EN_DICT.put("feels", "fiːlz");
        EN_DICT.put("felt", "fɛlt"); EN_DICT.put("feeling", "fiːlɪŋ");
        EN_DICT.put("try", "tɹaɪ"); EN_DICT.put("tries", "tɹaɪz");
        EN_DICT.put("tried", "tɹaɪd"); EN_DICT.put("trying", "tɹaɪɪŋ");
        EN_DICT.put("use", "juːz"); EN_DICT.put("uses", "juːzɪz");
        EN_DICT.put("used", "juːzd"); EN_DICT.put("using", "juːzɪŋ");
        EN_DICT.put("find", "faɪnd"); EN_DICT.put("finds", "faɪndz");
        EN_DICT.put("found", "faʊnd"); EN_DICT.put("finding", "faɪndɪŋ");
        EN_DICT.put("work", "wɜɹk"); EN_DICT.put("works", "wɜɹks");
        EN_DICT.put("worked", "wɜɹkt"); EN_DICT.put("working", "wɜɹkɪŋ");
        EN_DICT.put("call", "kɔːl"); EN_DICT.put("calls", "kɔːlz");
        EN_DICT.put("called", "kɔːld"); EN_DICT.put("calling", "kɔːlɪŋ");
        EN_DICT.put("help", "hɛlp"); EN_DICT.put("helps", "hɛlps");
        EN_DICT.put("helped", "hɛlpt"); EN_DICT.put("helping", "hɛlpɪŋ");
        EN_DICT.put("play", "pleɪ"); EN_DICT.put("plays", "pleɪz");
        EN_DICT.put("played", "pleɪd"); EN_DICT.put("playing", "pleɪɪŋ");
        EN_DICT.put("run", "ɹʌn"); EN_DICT.put("runs", "ɹʌnz");
        EN_DICT.put("ran", "ɹæn"); EN_DICT.put("running", "ɹʌnɪŋ");
        EN_DICT.put("walk", "wɔːk"); EN_DICT.put("walks", "wɔːks");
        EN_DICT.put("walked", "wɔːkt"); EN_DICT.put("walking", "wɔːkɪŋ");
        EN_DICT.put("talk", "tɔːk"); EN_DICT.put("talks", "tɔːks");
        EN_DICT.put("talked", "tɔːkt"); EN_DICT.put("talking", "tɔːkɪŋ");
        EN_DICT.put("ask", "æsk"); EN_DICT.put("asks", "æsks");
        EN_DICT.put("asked", "æskt"); EN_DICT.put("asking", "æskɪŋ");
        EN_DICT.put("eat", "iːt"); EN_DICT.put("eats", "iːts");
        EN_DICT.put("ate", "eɪt"); EN_DICT.put("eaten", "iːtən"); EN_DICT.put("eating", "iːtɪŋ");
        EN_DICT.put("drink", "dɹɪŋk"); EN_DICT.put("drinks", "dɹɪŋks");
        EN_DICT.put("drank", "dɹæŋk"); EN_DICT.put("drunk", "dɹʌŋk");
        EN_DICT.put("read", "ɹiːd"); EN_DICT.put("reads", "ɹiːdz");
        EN_DICT.put("reading", "ɹiːdɪŋ");
        EN_DICT.put("write", "ɹaɪt"); EN_DICT.put("writes", "ɹaɪts");
        EN_DICT.put("wrote", "ɹoʊt"); EN_DICT.put("written", "ɹɪtən");
        EN_DICT.put("learn", "lɜɹn"); EN_DICT.put("learns", "lɜɹnz");
        EN_DICT.put("learned", "lɜɹnd"); EN_DICT.put("learning", "lɜɹnɪŋ");
        EN_DICT.put("speak", "spiːk"); EN_DICT.put("speaks", "spiːks");
        EN_DICT.put("spoke", "spoʊk"); EN_DICT.put("spoken", "spoʊkən");
        EN_DICT.put("listen", "lɪsən"); EN_DICT.put("listens", "lɪsənz");
        EN_DICT.put("listened", "lɪsənd"); EN_DICT.put("listening", "lɪsənɪŋ");
        EN_DICT.put("look", "lʊk"); EN_DICT.put("looks", "lʊks");
        EN_DICT.put("looked", "lʊkt"); EN_DICT.put("looking", "lʊkɪŋ");
        EN_DICT.put("watch", "wɒʧ"); EN_DICT.put("watches", "wɒʧɪz");
        EN_DICT.put("watched", "wɒʧt"); EN_DICT.put("watching", "wɒʧɪŋ");
        EN_DICT.put("open", "oʊpən"); EN_DICT.put("opens", "oʊpənz");
        EN_DICT.put("opened", "oʊpənd"); EN_DICT.put("opening", "oʊpənɪŋ");
        EN_DICT.put("close", "kloʊz"); EN_DICT.put("closes", "kloʊzɪz");
        EN_DICT.put("closed", "kloʊzd"); EN_DICT.put("closing", "kloʊzɪŋ");
        EN_DICT.put("start", "stɑɹt"); EN_DICT.put("starts", "stɑɹts");
        EN_DICT.put("started", "stɑɹtɪd"); EN_DICT.put("starting", "stɑɹtɪŋ");
        EN_DICT.put("stop", "stɒp"); EN_DICT.put("stops", "stɒps");
        EN_DICT.put("stopped", "stɒpt"); EN_DICT.put("stopping", "stɒpɪŋ");
        EN_DICT.put("wait", "weɪt"); EN_DICT.put("waits", "weɪts");
        EN_DICT.put("waited", "weɪtɪd"); EN_DICT.put("waiting", "weɪtɪŋ");
        EN_DICT.put("move", "muːv"); EN_DICT.put("moves", "muːvz");
        EN_DICT.put("moved", "muːvd"); EN_DICT.put("moving", "muːvɪŋ");
        EN_DICT.put("live", "lɪv"); EN_DICT.put("lives", "lɪvz");
        EN_DICT.put("lived", "lɪvd"); EN_DICT.put("living", "lɪvɪŋ");
        EN_DICT.put("die", "daɪ"); EN_DICT.put("dies", "daɪz");
        EN_DICT.put("died", "daɪd"); EN_DICT.put("dying", "daɪɪŋ");
        EN_DICT.put("buy", "baɪ"); EN_DICT.put("buys", "baɪz");
        EN_DICT.put("bought", "bɔːt"); EN_DICT.put("buying", "baɪɪŋ");
        EN_DICT.put("sell", "sɛl"); EN_DICT.put("sells", "sɛlz");
        EN_DICT.put("sold", "soʊld"); EN_DICT.put("selling", "sɛlɪŋ");
        EN_DICT.put("pay", "peɪ"); EN_DICT.put("pays", "peɪz");
        EN_DICT.put("paid", "peɪd"); EN_DICT.put("paying", "peɪɪŋ");
        EN_DICT.put("send", "sɛnd"); EN_DICT.put("sends", "sɛndz");
        EN_DICT.put("sent", "sɛnt"); EN_DICT.put("sending", "sɛndɪŋ");
        EN_DICT.put("keep", "kiːp"); EN_DICT.put("keeps", "kiːpz");
        EN_DICT.put("kept", "kɛpt"); EN_DICT.put("keeping", "kiːpɪŋ");
        EN_DICT.put("let", "lɛt"); EN_DICT.put("lets", "lɛts");
        EN_DICT.put("leave", "liːv"); EN_DICT.put("leaves", "liːvz");
        EN_DICT.put("left", "lɛft"); EN_DICT.put("leaving", "liːvɪŋ");
        EN_DICT.put("begin", "bɪɡɪn"); EN_DICT.put("begins", "bɪɡɪnz");
        EN_DICT.put("began", "bɪɡæn"); EN_DICT.put("begun", "bɪɡʌn");
        EN_DICT.put("hold", "hoʊld"); EN_DICT.put("holds", "hoʊldz");
        EN_DICT.put("held", "hɛld"); EN_DICT.put("holding", "hoʊldɪŋ");
        EN_DICT.put("bring", "bɹɪŋ"); EN_DICT.put("brings", "bɹɪŋz");
        EN_DICT.put("brought", "bɹɔːt"); EN_DICT.put("bringing", "bɹɪŋɪŋ");
        EN_DICT.put("happen", "hæpən"); EN_DICT.put("happens", "hæpənz");
        EN_DICT.put("happened", "hæpənd"); EN_DICT.put("happening", "hæpənɪŋ");

        // Common words
        EN_DICT.put("the", "ðə"); EN_DICT.put("a", "ə"); EN_DICT.put("an", "æn");
        EN_DICT.put("and", "ænd"); EN_DICT.put("or", "ɔɹ"); EN_DICT.put("but", "bʌt");
        EN_DICT.put("if", "ɪf"); EN_DICT.put("then", "ðɛn");
        EN_DICT.put("so", "soʊ"); EN_DICT.put("as", "æz");
        EN_DICT.put("of", "ɒv"); EN_DICT.put("in", "ɪn"); EN_DICT.put("on", "ɒn");
        EN_DICT.put("at", "æt"); EN_DICT.put("to", "tu"); EN_DICT.put("for", "fɔɹ");
        EN_DICT.put("with", "wɪð"); EN_DICT.put("from", "fɹɒm");
        EN_DICT.put("by", "baɪ"); EN_DICT.put("about", "əbaʊt");
        EN_DICT.put("into", "ɪntu"); EN_DICT.put("through", "θɹu");
        EN_DICT.put("during", "djʊɹɪŋ"); EN_DICT.put("before", "bɪfɔɹ");
        EN_DICT.put("after", "æftəɹ"); EN_DICT.put("above", "əbʌv");
        EN_DICT.put("below", "bɪloʊ"); EN_DICT.put("between", "bɪtwiːn");
        EN_DICT.put("under", "ʌndəɹ"); EN_DICT.put("over", "oʊvəɹ");
        EN_DICT.put("up", "ʌp"); EN_DICT.put("down", "daʊn");
        EN_DICT.put("out", "aʊt"); EN_DICT.put("off", "ɒf");
        EN_DICT.put("again", "əɡɛn"); EN_DICT.put("also", "ɔːlsoʊ");
        EN_DICT.put("very", "vɛɹi"); EN_DICT.put("just", "ʤʌst");
        EN_DICT.put("only", "oʊnli"); EN_DICT.put("still", "stɪl");
        EN_DICT.put("even", "iːvən"); EN_DICT.put("never", "nɛvəɹ");
        EN_DICT.put("always", "ɔːlweɪz"); EN_DICT.put("often", "ɒfən");
        EN_DICT.put("sometimes", "sʌmtaɪmz"); EN_DICT.put("now", "naʊ");
        EN_DICT.put("here", "hiːɹ"); EN_DICT.put("there", "ðɛɹ");
        EN_DICT.put("yes", "jɛs"); EN_DICT.put("no", "noʊ");
        EN_DICT.put("not", "nɒt"); EN_DICT.put("please", "pliːz");
        EN_DICT.put("thank", "θæŋk"); EN_DICT.put("thanks", "θæŋks");
        EN_DICT.put("sorry", "sɒɹi"); EN_DICT.put("welcome", "wɛlkəm");
        EN_DICT.put("hello", "hɛloʊ"); EN_DICT.put("hi", "haɪ");
        EN_DICT.put("goodbye", "ɡʊdbaɪ"); EN_DICT.put("bye", "baɪ");
        EN_DICT.put("okay", "oʊkeɪ"); EN_DICT.put("ok", "oʊkeɪ");
        EN_DICT.put("maybe", "meɪbi"); EN_DICT.put("perhaps", "pəɹhæps");
        EN_DICT.put("really", "ɹiːli"); EN_DICT.put("actually", "ækʧuəli");
        EN_DICT.put("probably", "pɹɒbəbli");
        EN_DICT.put("everything", "ɛvɹiθɪŋ");
        EN_DICT.put("something", "sʌmθɪŋ");
        EN_DICT.put("nothing", "nʌθɪŋ");
        EN_DICT.put("anything", "ɛniθɪŋ");
        EN_DICT.put("everyone", "ɛvɹiwʌn");
        EN_DICT.put("someone", "sʌmwʌn");
        EN_DICT.put("anyone", "ɛniwʌn");
        EN_DICT.put("today", "tədeɪ"); EN_DICT.put("tomorrow", "təmɒɹoʊ");
        EN_DICT.put("yesterday", "jɛstəɹdeɪ");
        EN_DICT.put("morning", "mɔɹnɪŋ"); EN_DICT.put("afternoon", "æftəɹnun");
        EN_DICT.put("evening", "iːvnɪŋ"); EN_DICT.put("night", "naɪt");
        EN_DICT.put("time", "taɪm"); EN_DICT.put("year", "jiːɹ");
        EN_DICT.put("day", "deɪ"); EN_DICT.put("week", "wiːk");
        EN_DICT.put("month", "mʌnθ"); EN_DICT.put("hour", "aʊɹ");
        EN_DICT.put("minute", "mɪnɪt"); EN_DICT.put("second", "sɛkənd");
        EN_DICT.put("man", "mæn"); EN_DICT.put("men", "mɛn");
        EN_DICT.put("woman", "wʊmən"); EN_DICT.put("women", "wɪmɪn");
        EN_DICT.put("child", "ʧaɪld"); EN_DICT.put("children", "ʧɪldɹən");
        EN_DICT.put("boy", "bɔɪ"); EN_DICT.put("girl", "ɡɜɹl");
        EN_DICT.put("baby", "beɪbi"); EN_DICT.put("person", "pɜɹsən");
        EN_DICT.put("people", "piːpəl"); EN_DICT.put("friend", "fɹɛnd");
        EN_DICT.put("friends", "fɹɛndz"); EN_DICT.put("family", "fæməli");
        EN_DICT.put("mother", "mʌðəɹ"); EN_DICT.put("father", "fɑːðəɹ");
        EN_DICT.put("mom", "mɒm"); EN_DICT.put("dad", "dæd");
        EN_DICT.put("brother", "bɹʌðəɹ"); EN_DICT.put("sister", "sɪstəɹ");
        EN_DICT.put("son", "sʌn"); EN_DICT.put("daughter", "dɔːtəɹ");
        EN_DICT.put("husband", "hʌzbənd"); EN_DICT.put("wife", "waɪf");
        EN_DICT.put("home", "hoʊm"); EN_DICT.put("house", "haʊs");
        EN_DICT.put("room", "ɹuːm"); EN_DICT.put("door", "dɔɹ");
        EN_DICT.put("window", "wɪndoʊ"); EN_DICT.put("bed", "bɛd");
        EN_DICT.put("table", "teɪbəl"); EN_DICT.put("chair", "ʧɛɹ");
        EN_DICT.put("school", "skuːl"); EN_DICT.put("work", "wɜɹk");
        EN_DICT.put("job", "ʤɒb"); EN_DICT.put("money", "mʌni");
        EN_DICT.put("food", "fuːd"); EN_DICT.put("water", "wɔːtəɹ");
        EN_DICT.put("book", "bʊk"); EN_DICT.put("books", "bʊks");
        EN_DICT.put("car", "kɑɹ"); EN_DICT.put("cars", "kɑɹz");
        EN_DICT.put("phone", "foʊn"); EN_DICT.put("computer", "kəmpjuːtəɹ");
        EN_DICT.put("world", "wɜɹld"); EN_DICT.put("country", "kʌntɹi");
        EN_DICT.put("city", "sɪti"); EN_DICT.put("town", "taʊn");
        EN_DICT.put("street", "stɹiːt"); EN_DICT.put("road", "ɹoʊd");
        EN_DICT.put("way", "weɪ"); EN_DICT.put("place", "pleɪs");
        EN_DICT.put("thing", "θɪŋ"); EN_DICT.put("things", "θɪŋz");
        EN_DICT.put("life", "laɪf"); EN_DICT.put("death", "dɛθ");
        EN_DICT.put("love", "lʌv"); EN_DICT.put("heart", "hɑɹt");
        EN_DICT.put("mind", "maɪnd"); EN_DICT.put("body", "bɒdi");
        EN_DICT.put("head", "hɛd"); EN_DICT.put("hand", "hænd");
        EN_DICT.put("eye", "aɪ"); EN_DICT.put("eyes", "aɪz");
        EN_DICT.put("face", "feɪs"); EN_DICT.put("smile", "smaɪl");
        EN_DICT.put("happy", "hæpi"); EN_DICT.put("sad", "sæd");
        EN_DICT.put("angry", "æŋɡɹi"); EN_DICT.put("afraid", "əfɹeɪd");
        EN_DICT.put("good", "ɡʊd"); EN_DICT.put("bad", "bæd");
        EN_DICT.put("great", "ɡɹeɪt"); EN_DICT.put("nice", "naɪs");
        EN_DICT.put("beautiful", "bjuːtɪfəl"); EN_DICT.put("ugly", "ʌɡli");
        EN_DICT.put("big", "bɪɡ"); EN_DICT.put("small", "smɔːl");
        EN_DICT.put("long", "lɒŋ"); EN_DICT.put("short", "ʃɔɹt");
        EN_DICT.put("new", "njuː"); EN_DICT.put("old", "oʊld");
        EN_DICT.put("young", "jʌŋ"); EN_DICT.put("first", "fɜɹst");
        EN_DICT.put("last", "læst"); EN_DICT.put("next", "nɛkst");
        EN_DICT.put("right", "ɹaɪt"); EN_DICT.put("left", "lɛft");
        EN_DICT.put("high", "haɪ"); EN_DICT.put("low", "loʊ");
        EN_DICT.put("hot", "hɒt"); EN_DICT.put("cold", "koʊld");
        EN_DICT.put("warm", "wɔɹm"); EN_DICT.put("cool", "kuːl");
        EN_DICT.put("fast", "fæst"); EN_DICT.put("slow", "sloʊ");
        EN_DICT.put("hard", "hɑɹd"); EN_DICT.put("easy", "iːzi");
        EN_DICT.put("true", "tɹuː"); EN_DICT.put("false", "fɔːls");
        EN_DICT.put("real", "ɹiːəl"); EN_DICT.put("important", "ɪmpɔɹtənt");
        EN_DICT.put("different", "dɪfɹənt"); EN_DICT.put("same", "seɪm");
        EN_DICT.put("special", "spɛʃəl"); EN_DICT.put("possible", "pɒsɪbəl");
        EN_DICT.put("white", "waɪt"); EN_DICT.put("black", "blæk");
        EN_DICT.put("red", "ɹɛd"); EN_DICT.put("blue", "bluː");
        EN_DICT.put("green", "ɡɹiːn"); EN_DICT.put("yellow", "jɛloʊ");
        EN_DICT.put("one", "wʌn"); EN_DICT.put("two", "tuː");
        EN_DICT.put("three", "θɹiː"); EN_DICT.put("four", "fɔɹ");
        EN_DICT.put("five", "faɪv"); EN_DICT.put("six", "sɪks");
        EN_DICT.put("seven", "sɛvən"); EN_DICT.put("eight", "eɪt");
        EN_DICT.put("nine", "naɪn"); EN_DICT.put("ten", "tɛn");
        EN_DICT.put("hundred", "hʌndɹəd"); EN_DICT.put("thousand", "θaʊzənd");
        EN_DICT.put("million", "mɪljən");
        EN_DICT.put("test", "tɛst"); EN_DICT.put("tests", "tɛsts");
        EN_DICT.put("speech", "spiʧ"); EN_DICT.put("text", "tɛkst");
        EN_DICT.put("voice", "vɔɪs"); EN_DICT.put("audio", "ɔːdioʊ");
        EN_DICT.put("sound", "saʊnd"); EN_DICT.put("music", "mjuːzɪk");
        EN_DICT.put("word", "wɜɹd"); EN_DICT.put("words", "wɜɹdz");
        EN_DICT.put("language", "læŋɡwɪʤ"); EN_DICT.put("english", "ɪŋɡlɪʃ");
        EN_DICT.put("kokoro", "koʊkoʊɹoʊ");

        // ============ HINDI DICTIONARY (100+ words) ============
        // Greetings & common
        HI_DICT.put("नमस्ते", "nəmasteː");
        HI_DICT.put("नमस्कार", "nəmskɑːɹ");
        HI_DICT.put("धन्यवाद", "d̪ʱənjiəʋɑːd̪");
        HI_DICT.put("शुक्रिया", "ʃʊkɹiə");
        HI_DICT.put("हाँ", "ɦɑ̃ː");
        HI_DICT.put("हां", "ɦɑ̃ː");
        HI_DICT.put("नहीं", "nəɦĩː");
        HI_DICT.put("नही", "nəɦiː");
        HI_DICT.put("जी", "ʤiː");
        HI_DICT.put("अच्छा", "ətʃʰɑː");
        HI_DICT.put("ठीक", "t̪ʰiːk");
        HI_DICT.put("सही", "səɦiː");
        HI_DICT.put("गलत", "ɡələt̪");

        // Pronouns
        HI_DICT.put("मैं", "mɛ̃ː");
        HI_DICT.put("तू", "t̪uː");
        HI_DICT.put("तुम", "t̪ʊm");
        HI_DICT.put("आप", "ɑːp");
        HI_DICT.put("हम", "ɦəm");
        HI_DICT.put("वह", "ʋəɦ");
        HI_DICT.put("वो", "ʋoː");
        HI_DICT.put("वे", "ʋeː");
        HI_DICT.put("यह", "jəɦ");
        HI_DICT.put("ये", "jeː");
        HI_DICT.put("इस", "ɪs");
        HI_DICT.put("उस", "ʊs");
        HI_DICT.put("इन", "ɪn");
        HI_DICT.put("उन", "ʊn");

        // Question words
        HI_DICT.put("क्या", "kjɑː");
        HI_DICT.put("कैसे", "kɛːseː");
        HI_DICT.put("कैसा", "kɛːsɑː");
        HI_DICT.put("कैसी", "kɛːsiː");
        HI_DICT.put("कब", "kəb");
        HI_DICT.put("कहाँ", "kəɦɑ̃ː");
        HI_DICT.put("कहां", "kəɦɑ̃ː");
        HI_DICT.put("क्यों", "kjõː");
        HI_DICT.put("क्योंकि", "kjõːkiː");
        HI_DICT.put("कौन", "kɔːn");
        HI_DICT.put("कितना", "kɪt̪nɑː");
        HI_DICT.put("कितनी", "kɪt̪niː");
        HI_DICT.put("कितने", "kɪt̪neː");

        // Verbs
        HI_DICT.put("है", "hɛː");
        HI_DICT.put("हैं", "hɛ̃ː");
        HI_DICT.put("था", "t̪ʰɑː");
        HI_DICT.put("थी", "t̪ʰiː");
        HI_DICT.put("थे", "t̪ʰeː");
        HI_DICT.put("हूँ", "ɦũː");
        HI_DICT.put("हूं", "ɦũː");
        HI_DICT.put("हो", "ɦoː");
        HI_DICT.put("होगा", "ɦoːɡɑː");
        HI_DICT.put("होगी", "ɦoːɡiː");
        HI_DICT.put("होंगे", "ɦõːɡeː");
        HI_DICT.put("कर", "kəɹ");
        HI_DICT.put("करना", "kəɹnɑː");
        HI_DICT.put("किया", "kiə");
        HI_DICT.put("कैसे", "kɛːseː");
        HI_DICT.put("जा", "ʤɑː");
        HI_DICT.put("जाना", "ʤɑːnɑː");
        HI_DICT.put("गया", "ɡəjɑː");
        HI_DICT.put("गई", "ɡəjiː");
        HI_DICT.put("गए", "ɡəeː");
        HI_DICT.put("आ", "ɑː");
        HI_DICT.put("आना", "ɑːnɑː");
        HI_DICT.put("आया", "ɑːjɑː");
        HI_DICT.put("आई", "ɑːjiː");
        HI_DICT.put("आए", "ɑːeː");
        HI_DICT.put("दे", "d̪eː");
        HI_DICT.put("देना", "d̪eːnɑː");
        HI_DICT.put("दिया", "d̪iə");
        HI_DICT.put("ले", "leː");
        HI_DICT.put("लेना", "leːnɑː");
        HI_DICT.put("लिया", "liə");
        HI_DICT.put("बोल", "boːl");
        HI_DICT.put("बोलना", "boːlnɑː");
        HI_DICT.put("बोला", "boːlɑː");
        HI_DICT.put("सुन", "sʊn");
        HI_DICT.put("सुनना", "sʊnnɑː");
        HI_DICT.put("सुना", "sʊnɑː");
        HI_DICT.put("देख", "d̪eːkʰ");
        HI_DICT.put("देखना", "d̪eːkʰnɑː");
        HI_DICT.put("देखा", "d̪eːkʰɑː");
        HI_DICT.put("खा", "kʰɑː");
        HI_DICT.put("खाना", "kʰɑːnɑː");
        HI_DICT.put("खाया", "kʰɑːjɑː");
        HI_DICT.put("पी", "piː");
        HI_DICT.put("पीना", "piːnɑː");
        HI_DICT.put("पिया", "piə");

        // Common phrases
        HI_DICT.put("आप", "ɑːp");
        HI_DICT.put("कैसे", "kɛːseː");
        HI_DICT.put("हो", "ɦoː");
        HI_DICT.put("भारत", "bʱɑːɹət̪");
        HI_DICT.put("हिंदी", "ɦind̪iː");
        HI_DICT.put("हिन्दी", "ɦind̪iː");
        HI_DICT.put("इंग्लिश", "iŋɡlɪʃ");
        HI_DICT.put("अंग्रेजी", "əŋɡɹeːʤiː");
        HI_DICT.put("दुनिया", "d̪ʊniə");
        HI_DICT.put("ज़िंदगी", "zind̪ɡiː");
        HI_DICT.put("जिंदगी", "ʤind̪ɡiː");
        HI_DICT.put("प्यार", "pjɑːɹ");
        HI_DICT.put("दोस्त", "d̪oːst̪");
        HI_DICT.put("दोस्ती", "d̪oːst̪iː");
        HI_DICT.put("परिवार", "pəɹiʋɑːɹ");
        HI_DICT.put("घर", "ɡʱəɹ");
        HI_DICT.put("पानी", "pɑːniː");
        HI_DICT.put("खाना", "kʰɑːnɑː");
        HI_DICT.put("रोटी", "ɹoːʈiː");
        HI_DICT.put("चाय", "ʧɑːeː");
        HI_DICT.put("किताब", "kit̪ɑːb");
        HI_DICT.put("स्कूल", "skuːl");
        HI_DICT.put("काम", "kɑːm");
        HI_DICT.put("पैसा", "pɛːsɑː");
        HI_DICT.put("समय", "səməeː");
        HI_DICT.put("वक्त", "ʋəkt̪");
        HI_DICT.put("दिन", "d̪in");
        HI_DICT.put("रात", "ɹɑːt̪");
        HI_DICT.put("सुबह", "sʊbəɦ");
        HI_DICT.put("शाम", "ʃɑːm");
        HI_DICT.put("आज", "ɑːʤ");
        HI_DICT.put("कल", "kəl");
        HI_DICT.put("परसों", "pəɹsõː");

        // Numbers
        HI_DICT.put("एक", "eːk");
        HI_DICT.put("दो", "d̪oː");
        HI_DICT.put("तीन", "t̪iːn");
        HI_DICT.put("चार", "ʧɑːɹ");
        HI_DICT.put("पांच", "pɑːnʧ");
        HI_DICT.put("छह", "ʧʰəɦ");
        HI_DICT.put("सात", "sɑːt̪");
        HI_DICT.put("आठ", "ɑːʈʰ");
        HI_DICT.put("नौ", "nɔː");
        HI_DICT.put("दस", "d̪əs");

        // ============ NUMBER CONVERSION ============
        NUM_EN.put("0", "ziːɹoʊ"); NUM_EN.put("1", "wʌn");
        NUM_EN.put("2", "tuː"); NUM_EN.put("3", "θɹiː");
        NUM_EN.put("4", "fɔɹ"); NUM_EN.put("5", "faɪv");
        NUM_EN.put("6", "sɪks"); NUM_EN.put("7", "sɛvən");
        NUM_EN.put("8", "eɪt"); NUM_EN.put("9", "naɪn");
        NUM_EN.put("10", "tɛn"); NUM_EN.put("100", "hʌndɹəd");
        NUM_EN.put("1000", "θaʊzənd");

        NUM_HI.put("०", "ʃʊnjiə"); NUM_HI.put("१", "eːk");
        NUM_HI.put("२", "d̪oː"); NUM_HI.put("३", "t̪iːn");
        NUM_HI.put("४", "ʧɑːɹ"); NUM_HI.put("५", "pɑːnʧ");
        NUM_HI.put("६", "ʧʰəɦ"); NUM_HI.put("७", "sɑːt̪");
        NUM_HI.put("८", "ɑːʈʰ"); NUM_HI.put("९", "nɔː");
    }

    public static String phonemize(String text, String langCode) {
        if (text == null || text.trim().isEmpty()) return "";
        text = text.trim();
        return langCode.equals("a") ? phonemizeEnglish(text) : phonemizeHindi(text);
    }

    // ============ ENGLISH ============
    private static String phonemizeEnglish(String text) {
        // Pre-process: normalize punctuation to IPA pauses
        text = text.toLowerCase();
        text = text.replace(",", " , ");
        text = text.replace(".", " . ");
        text = text.replace("!", " ! ");
        text = text.replace("?", " ? ");
        text = text.replace(";", " ; ");
        text = text.replace(":", " : ");
        text = text.replace("-", " ");
        text = text.replaceAll("\\s+", " ").trim();

        StringBuilder result = new StringBuilder();
        String[] tokens = text.split("\\s+");
        boolean first = true;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            // Punctuation → pauses
            if (token.equals(",")) { result.append(","); first = false; continue; }
            if (token.equals(".")) { result.append("."); first = false; continue; }
            if (token.equals("!")) { result.append("!"); first = false; continue; }
            if (token.equals("?")) { result.append("?"); first = false; continue; }
            if (token.equals(";")) { result.append(";"); first = false; continue; }
            if (token.equals(":")) { result.append(":"); first = false; continue; }

            // Strip surrounding punctuation for lookup
            String clean = token.replaceAll("[^a-z0-9']", "");
            if (clean.isEmpty()) continue;

            String ipa = null;

            // Try dictionary
            ipa = EN_DICT.get(clean);

            // Try number conversion
            if (ipa == null && clean.matches("\\d+")) {
                ipa = numberToIpaEn(clean);
            }

            // Fallback
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
        // For multi-digit numbers, read digit by digit
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
            char prev = (i > 0) ? word.charAt(i - 1) : ' ';

            if (c == 'a') {
                if (next == 'i') ipa.append("eɪ");
                else if (next == 'u') ipa.append("ɔː");
                else if (next == 'l' && (next == ' ' || i + 2 >= word.length())) ipa.append("ɔːl");
                else if (prev == 'e' || (i == word.length() - 2 && next == 'e')) ipa.append("eɪ");
                else ipa.append("æ");
            } else if (c == 'e') {
                if (next == 'a' || next == 'i') ipa.append("iː");
                else if (i == word.length() - 1) { /* silent e */ }
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
            } else if (c == 'g') {
                ipa.append("ɡ");  // U+0261
            } else if (c == 't' && next == 'h') {
                ipa.append("θ"); i++;
            } else if (c == 's' && next == 'h') {
                ipa.append("ʃ"); i++;
            } else if (c == 'c' && next == 'h') {
                ipa.append("ʧ"); i++;
            } else if (c == 'c' && (next == 'e' || next == 'i' || next == 'y')) {
                ipa.append("s");
            } else if (c == 'c') {
                ipa.append("k");
            } else if (c == 'g' && (next == 'e' || next == 'i')) {
                ipa.append("ʤ");
            } else if (c == 'n' && next == 'g') {
                ipa.append("ŋ"); i++;
            } else if (c == 'w') {
                ipa.append("w");
            } else if (c == 'y') {
                if (i == 0) ipa.append("j");
                else if (i == word.length() - 1) ipa.append("iː");
                else ipa.append("ɪ");
            } else if (c == 'x') {
                ipa.append("ks");
            } else if (c == 'q') {
                ipa.append("kw");
            } else if (c == 'z') {
                ipa.append("z");
            } else if (c == '\'') {
                // skip apostrophes
            } else if (Character.isLetter(c)) {
                ipa.append(c);
            }
        }
        return ipa.toString();
    }

    // ============ HINDI ============
    private static String phonemizeHindi(String text) {
        // Pre-process: normalize punctuation
        text = text.replace(",", " , ");
        text = text.replace(".", " . ");
        text = text.replace("!", " ! ");
        text = text.replace("?", " ? ");
        text = text.replace(";", " ; ");
        text = text.replace(":", " : ");
        text = text.replaceAll("\\s+", " ").trim();

        StringBuilder result = new StringBuilder();
        String[] tokens = text.split("\\s+");
        boolean first = true;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            // Punctuation → pauses
            if (token.equals(",")) { result.append(","); first = false; continue; }
            if (token.equals(".")) { result.append("."); first = false; continue; }
            if (token.equals("!")) { result.append("!"); first = false; continue; }
            if (token.equals("?")) { result.append("?"); first = false; continue; }
            if (token.equals(";")) { result.append(";"); first = false; continue; }
            if (token.equals(":")) { result.append(":"); first = false; continue; }

            String ipa = null;

            // Try dictionary
            ipa = HI_DICT.get(token);

            // Try Hindi number conversion
            if (ipa == null && token.matches("[०-९]+")) {
                ipa = numberToIpaHi(token);
            }

            // Try ASCII number conversion
            if (ipa == null && token.matches("\\d+")) {
                ipa = numberToIpaEn(token);
            }

            // Fallback: character-by-character Devanagari → IPA
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
            // Vowels (independent)
            if (c == 'अ') ipa.append("ə");
            else if (c == 'आ' || c == 'ा') ipa.append("ɑː");
            else if (c == 'इ' || c == 'ि') ipa.append("ɪ");
            else if (c == 'ई' || c == 'ी') ipa.append("iː");
            else if (c == 'उ' || c == 'ु') ipa.append("ʊ");
            else if (c == 'ऊ' || c == 'ू') ipa.append("uː");
            else if (c == 'ऋ' || c == 'ृ') ipa.append("ɾɪ");
            else if (c == 'ए' || c == 'े') ipa.append("eː");
            else if (c == 'ऐ' || c == 'ै') ipa.append("ɛː");
            else if (c == 'ओ' || c == 'ो') ipa.append("oː");
            else if (c == 'औ' || c == 'ौ') ipa.append("ɔː");
            // Consonants
            else if (c == 'क') ipa.append("kə");
            else if (c == 'ख') ipa.append("kʰə");
            else if (c == 'ग') ipa.append("ɡə");
            else if (c == 'घ') ipa.append("ɡʱə");
            else if (c == 'ङ') ipa.append("ŋə");
            else if (c == 'च') ipa.append("ʧə");
            else if (c == 'छ') ipa.append("ʧʰə");
            else if (c == 'ज') ipa.append("ʤə");
            else if (c == 'झ') ipa.append("ʤʱə");
            else if (c == 'ञ') ipa.append("ɲə");
            else if (c == 'ट') ipa.append("ʈə");
            else if (c == 'ठ') ipa.append("ʈʰə");
            else if (c == 'ड') ipa.append("ɖə");
            else if (c == 'ढ') ipa.append("ɖʱə");
            else if (c == 'ण') ipa.append("ɳə");
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
            else if (c == 'ष') ipa.append("ʂə");
            else if (c == 'स') ipa.append("sə");
            else if (c == 'ह') ipa.append("ɦə");
            else if (c == 'ळ') ipa.append("ɭə");
            else if (c == 'क्ष') ipa.append("kʂə");
            else if (c == 'त्र') ipa.append("t̪ɾə");
            else if (c == 'ज्ञ') ipa.append("ʤɲə");
            // Matras (dependent vowels) - replace the inherent 'ə'
            else if (c == 'ा') { replaceLastSchwa(ipa, "ɑː"); }
            else if (c == 'ि') { replaceLastSchwa(ipa, "ɪ"); }
            else if (c == 'ी') { replaceLastSchwa(ipa, "iː"); }
            else if (c == 'ु') { replaceLastSchwa(ipa, "ʊ"); }
            else if (c == 'ू') { replaceLastSchwa(ipa, "uː"); }
            else if (c == 'े') { replaceLastSchwa(ipa, "eː"); }
            else if (c == 'ै') { replaceLastSchwa(ipa, "ɛː"); }
            else if (c == 'ो') { replaceLastSchwa(ipa, "oː"); }
            else if (c == 'ौ') { replaceLastSchwa(ipa, "ɔː"); }
            // Special signs
            else if (c == '्') {
                // Halant: remove inherent schwa
                int len = ipa.length();
                if (len >= 1 && ipa.substring(len - 1).equals("ə")) {
                    ipa.delete(len - 1, len);
                }
            }
            else if (c == 'ं') ipa.append("̃");
            else if (c == 'ँ') ipa.append("̃");
            else if (c == 'ः') ipa.append("h");
            // Numbers
            else if (c >= '०' && c <= '९') {
                String d = NUM_HI.get(String.valueOf(c));
                if (d != null) ipa.append(d);
            }
        }
        return ipa.toString();
    }

    private static void replaceLastSchwa(StringBuilder ipa, String replacement) {
        int len = ipa.length();
        if (len >= 1 && ipa.substring(len - 1).equals("ə")) {
            ipa.delete(len - 1, len);
        }
        ipa.append(replacement);
    }
}
