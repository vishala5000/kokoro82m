package com.example.kokorotts;

import java.util.HashMap;
import java.util.Map;

public class Phonemizer {

    private static final Map<String, String> EN_DICT = new HashMap<>();
    private static final Map<String, String> HI_DICT = new HashMap<>();
    private static final Map<String, String> NUM_EN = new HashMap<>();
    private static final Map<String, String> NUM_HI = new HashMap<>();
    private static final Map<String, String> ABBR_EN = new HashMap<>();

    static {
        // ============ ENGLISH WITH WORD STRESS (ˈ) ============
        // Pronouns
        EN_DICT.put("i", "ˈaɪ"); EN_DICT.put("me", "ˈmi"); EN_DICT.put("my", "ˈmaɪ");
        EN_DICT.put("mine", "ˈmaɪn"); EN_DICT.put("myself", "maɪˈsɛlf");
        EN_DICT.put("you", "ˈju"); EN_DICT.put("your", "ˈjɔɹ"); EN_DICT.put("yours", "ˈjɔɹz");
        EN_DICT.put("yourself", "jɔɹˈsɛlf");
        EN_DICT.put("he", "ˈhi"); EN_DICT.put("him", "ˈhɪm"); EN_DICT.put("his", "ˈhɪz");
        EN_DICT.put("she", "ˈʃi"); EN_DICT.put("her", "ˈhɜɹ"); EN_DICT.put("hers", "ˈhɜɹz");
        EN_DICT.put("it", "ˈɪt"); EN_DICT.put("its", "ˈɪts");
        EN_DICT.put("we", "ˈwi"); EN_DICT.put("us", "ˈʌs"); EN_DICT.put("our", "ˈaʊɹ");
        EN_DICT.put("ours", "ˈaʊɹz");
        EN_DICT.put("they", "ˈðeɪ"); EN_DICT.put("them", "ˈðɛm"); EN_DICT.put("their", "ˈðɛɹ");
        EN_DICT.put("theirs", "ˈðɛɹz");
        EN_DICT.put("this", "ˈðɪs"); EN_DICT.put("that", "ˈðæt");
        EN_DICT.put("these", "ˈðiːz"); EN_DICT.put("those", "ˈðoʊz");
        EN_DICT.put("who", "ˈhu"); EN_DICT.put("whom", "ˈhum"); EN_DICT.put("whose", "ˈhuz");
        EN_DICT.put("what", "ˈwɒt"); EN_DICT.put("which", "ˈwɪʧ");
        EN_DICT.put("where", "ˈwɛɹ"); EN_DICT.put("when", "ˈwɛn");
        EN_DICT.put("why", "ˈwaɪ"); EN_DICT.put("how", "ˈhaʊ");

        // Verbs (with stress)
        EN_DICT.put("am", "ˈæm"); EN_DICT.put("is", "ˈɪz"); EN_DICT.put("are", "ˈɑɹ");
        EN_DICT.put("was", "ˈwɒz"); EN_DICT.put("were", "ˈwɜɹ");
        EN_DICT.put("be", "ˈbi"); EN_DICT.put("been", "ˈbɪn"); EN_DICT.put("being", "ˈbiːɪŋ");
        EN_DICT.put("have", "ˈhæv"); EN_DICT.put("has", "ˈhæz"); EN_DICT.put("had", "ˈhæd");
        EN_DICT.put("do", "ˈdu"); EN_DICT.put("does", "ˈdʌz"); EN_DICT.put("did", "ˈdɪd");
        EN_DICT.put("done", "ˈdʌn"); EN_DICT.put("doing", "ˈduːɪŋ");
        EN_DICT.put("will", "ˈwɪl"); EN_DICT.put("would", "ˈwʊd");
        EN_DICT.put("can", "ˈkæn"); EN_DICT.put("could", "ˈkʊd");
        EN_DICT.put("shall", "ˈʃæl"); EN_DICT.put("should", "ˈʃʊd");
        EN_DICT.put("may", "ˈmeɪ"); EN_DICT.put("might", "ˈmaɪt");
        EN_DICT.put("must", "ˈmʌst");
        EN_DICT.put("go", "ˈɡoʊ"); EN_DICT.put("goes", "ˈɡoʊz");
        EN_DICT.put("went", "ˈwɛnt"); EN_DICT.put("gone", "ˈɡɒn"); EN_DICT.put("going", "ˈɡoʊɪŋ");
        EN_DICT.put("come", "ˈkʌm"); EN_DICT.put("comes", "ˈkʌmz");
        EN_DICT.put("came", "ˈkeɪm"); EN_DICT.put("coming", "ˈkʌmɪŋ");
        EN_DICT.put("see", "ˈsiː"); EN_DICT.put("sees", "ˈsiːz");
        EN_DICT.put("saw", "ˈsɔː"); EN_DICT.put("seen", "ˈsiːn"); EN_DICT.put("seeing", "ˈsiːɪŋ");
        EN_DICT.put("know", "ˈnoʊ"); EN_DICT.put("knows", "ˈnoʊz");
        EN_DICT.put("knew", "ˈnjuː"); EN_DICT.put("known", "ˈnoʊn"); EN_DICT.put("knowing", "ˈnoʊɪŋ");
        EN_DICT.put("think", "ˈθɪŋk"); EN_DICT.put("thinks", "ˈθɪŋks");
        EN_DICT.put("thought", "ˈθɔːt"); EN_DICT.put("thinking", "ˈθɪŋkɪŋ");
        EN_DICT.put("make", "ˈmeɪk"); EN_DICT.put("makes", "ˈmeɪks");
        EN_DICT.put("made", "ˈmeɪd"); EN_DICT.put("making", "ˈmeɪkɪŋ");
        EN_DICT.put("take", "ˈteɪk"); EN_DICT.put("takes", "ˈteɪks");
        EN_DICT.put("took", "ˈtʊk"); EN_DICT.put("taken", "ˈteɪkən"); EN_DICT.put("taking", "ˈteɪkɪŋ");
        EN_DICT.put("give", "ˈɡɪv"); EN_DICT.put("gives", "ˈɡɪvz");
        EN_DICT.put("gave", "ˈɡeɪv"); EN_DICT.put("given", "ˈɡɪvən"); EN_DICT.put("giving", "ˈɡɪvɪŋ");
        EN_DICT.put("say", "ˈseɪ"); EN_DICT.put("says", "ˈsɛz");
        EN_DICT.put("said", "ˈsɛd"); EN_DICT.put("saying", "ˈseɪɪŋ");
        EN_DICT.put("tell", "ˈtɛl"); EN_DICT.put("tells", "ˈtɛlz");
        EN_DICT.put("told", "ˈtoʊld"); EN_DICT.put("telling", "ˈtɛlɪŋ");
        EN_DICT.put("get", "ˈɡɛt"); EN_DICT.put("gets", "ˈɡɛts");
        EN_DICT.put("got", "ˈɡɒt"); EN_DICT.put("gotten", "ˈɡɒtən");
        EN_DICT.put("want", "ˈwɒnt"); EN_DICT.put("wants", "ˈwɒnts"); EN_DICT.put("wanted", "ˈwɒntɪd");
        EN_DICT.put("need", "ˈniːd"); EN_DICT.put("needs", "ˈniːdz"); EN_DICT.put("needed", "ˈniːdɪd");
        EN_DICT.put("like", "ˈlaɪk"); EN_DICT.put("likes", "ˈlaɪks"); EN_DICT.put("liked", "ˈlaɪkt");
        EN_DICT.put("love", "ˈlʌv"); EN_DICT.put("loves", "ˈlʌvz"); EN_DICT.put("loved", "ˈlʌvd");
        EN_DICT.put("hate", "ˈheɪt"); EN_DICT.put("hates", "ˈheɪts");
        EN_DICT.put("feel", "ˈfiːl"); EN_DICT.put("feels", "ˈfiːlz");
        EN_DICT.put("felt", "ˈfɛlt"); EN_DICT.put("feeling", "ˈfiːlɪŋ");
        EN_DICT.put("try", "ˈtɹaɪ"); EN_DICT.put("tries", "ˈtɹaɪz");
        EN_DICT.put("tried", "ˈtɹaɪd"); EN_DICT.put("trying", "ˈtɹaɪɪŋ");
        EN_DICT.put("use", "ˈjuːz"); EN_DICT.put("uses", "ˈjuːzɪz");
        EN_DICT.put("used", "ˈjuːzd"); EN_DICT.put("using", "ˈjuːzɪŋ");
        EN_DICT.put("find", "ˈfaɪnd"); EN_DICT.put("finds", "ˈfaɪndz");
        EN_DICT.put("found", "ˈfaʊnd"); EN_DICT.put("finding", "ˈfaɪndɪŋ");
        EN_DICT.put("work", "ˈwɜɹk"); EN_DICT.put("works", "ˈwɜɹks");
        EN_DICT.put("worked", "ˈwɜɹkt"); EN_DICT.put("working", "ˈwɜɹkɪŋ");
        EN_DICT.put("call", "ˈkɔːl"); EN_DICT.put("calls", "ˈkɔːlz");
        EN_DICT.put("called", "ˈkɔːld"); EN_DICT.put("calling", "ˈkɔːlɪŋ");
        EN_DICT.put("help", "ˈhɛlp"); EN_DICT.put("helps", "ˈhɛlps");
        EN_DICT.put("helped", "ˈhɛlpt"); EN_DICT.put("helping", "ˈhɛlpɪŋ");
        EN_DICT.put("play", "ˈpleɪ"); EN_DICT.put("plays", "ˈpleɪz");
        EN_DICT.put("played", "ˈpleɪd"); EN_DICT.put("playing", "ˈpleɪɪŋ");
        EN_DICT.put("run", "ˈɹʌn"); EN_DICT.put("runs", "ˈɹʌnz");
        EN_DICT.put("ran", "ˈɹæn"); EN_DICT.put("running", "ˈɹʌnɪŋ");
        EN_DICT.put("walk", "ˈwɔːk"); EN_DICT.put("walks", "ˈwɔːks");
        EN_DICT.put("walked", "ˈwɔːkt"); EN_DICT.put("walking", "ˈwɔːkɪŋ");
        EN_DICT.put("talk", "ˈtɔːk"); EN_DICT.put("talks", "ˈtɔːks");
        EN_DICT.put("talked", "ˈtɔːkt"); EN_DICT.put("talking", "ˈtɔːkɪŋ");
        EN_DICT.put("ask", "ˈæsk"); EN_DICT.put("asks", "ˈæsks");
        EN_DICT.put("asked", "ˈæskt"); EN_DICT.put("asking", "ˈæskɪŋ");
        EN_DICT.put("eat", "ˈiːt"); EN_DICT.put("eats", "ˈiːts");
        EN_DICT.put("ate", "ˈeɪt"); EN_DICT.put("eaten", "ˈiːtən"); EN_DICT.put("eating", "ˈiːtɪŋ");
        EN_DICT.put("drink", "ˈdɹɪŋk"); EN_DICT.put("drinks", "ˈdɹɪŋks");
        EN_DICT.put("drank", "ˈdɹæŋk"); EN_DICT.put("drunk", "ˈdɹʌŋk");
        EN_DICT.put("read", "ˈɹiːd"); EN_DICT.put("reads", "ˈɹiːdz");
        EN_DICT.put("reading", "ˈɹiːdɪŋ");
        EN_DICT.put("write", "ˈɹaɪt"); EN_DICT.put("writes", "ˈɹaɪts");
        EN_DICT.put("wrote", "ˈɹoʊt"); EN_DICT.put("written", "ˈɹɪtən");
        EN_DICT.put("learn", "ˈlɜɹn"); EN_DICT.put("learns", "ˈlɜɹnz");
        EN_DICT.put("learned", "ˈlɜɹnd"); EN_DICT.put("learning", "ˈlɜɹnɪŋ");
        EN_DICT.put("speak", "ˈspiːk"); EN_DICT.put("speaks", "ˈspiːks");
        EN_DICT.put("spoke", "ˈspoʊk"); EN_DICT.put("spoken", "ˈspoʊkən");
        EN_DICT.put("listen", "ˈlɪsən"); EN_DICT.put("listens", "ˈlɪsənz");
        EN_DICT.put("listened", "ˈlɪsənd"); EN_DICT.put("listening", "ˈlɪsənɪŋ");
        EN_DICT.put("look", "ˈlʊk"); EN_DICT.put("looks", "ˈlʊks");
        EN_DICT.put("looked", "ˈlʊkt"); EN_DICT.put("looking", "ˈlʊkɪŋ");
        EN_DICT.put("watch", "ˈwɒʧ"); EN_DICT.put("watches", "ˈwɒʧɪz");
        EN_DICT.put("watched", "ˈwɒʧt"); EN_DICT.put("watching", "ˈwɒʧɪŋ");
        EN_DICT.put("open", "ˈoʊpən"); EN_DICT.put("opens", "ˈoʊpənz");
        EN_DICT.put("opened", "ˈoʊpənd"); EN_DICT.put("opening", "ˈoʊpənɪŋ");
        EN_DICT.put("close", "ˈkloʊz"); EN_DICT.put("closes", "ˈkloʊzɪz");
        EN_DICT.put("closed", "ˈkloʊzd"); EN_DICT.put("closing", "ˈkloʊzɪŋ");
        EN_DICT.put("start", "ˈstɑɹt"); EN_DICT.put("starts", "ˈstɑɹts");
        EN_DICT.put("started", "ˈstɑɹtɪd"); EN_DICT.put("starting", "ˈstɑɹtɪŋ");
        EN_DICT.put("stop", "ˈstɒp"); EN_DICT.put("stops", "ˈstɒps");
        EN_DICT.put("stopped", "ˈstɒpt"); EN_DICT.put("stopping", "ˈstɒpɪŋ");
        EN_DICT.put("wait", "ˈweɪt"); EN_DICT.put("waits", "ˈweɪts");
        EN_DICT.put("waited", "ˈweɪtɪd"); EN_DICT.put("waiting", "ˈweɪtɪŋ");
        EN_DICT.put("move", "ˈmuːv"); EN_DICT.put("moves", "ˈmuːvz");
        EN_DICT.put("moved", "ˈmuːvd"); EN_DICT.put("moving", "ˈmuːvɪŋ");
        EN_DICT.put("live", "ˈlɪv"); EN_DICT.put("lives", "ˈlɪvz");
        EN_DICT.put("lived", "ˈlɪvd"); EN_DICT.put("living", "ˈlɪvɪŋ");
        EN_DICT.put("die", "ˈdaɪ"); EN_DICT.put("dies", "ˈdaɪz");
        EN_DICT.put("died", "ˈdaɪd"); EN_DICT.put("dying", "ˈdaɪɪŋ");
        EN_DICT.put("buy", "ˈbaɪ"); EN_DICT.put("buys", "ˈbaɪz");
        EN_DICT.put("bought", "ˈbɔːt"); EN_DICT.put("buying", "ˈbaɪɪŋ");
        EN_DICT.put("sell", "ˈsɛl"); EN_DICT.put("sells", "ˈsɛlz");
        EN_DICT.put("sold", "ˈsoʊld"); EN_DICT.put("selling", "ˈsɛlɪŋ");
        EN_DICT.put("pay", "ˈpeɪ"); EN_DICT.put("pays", "ˈpeɪz");
        EN_DICT.put("paid", "ˈpeɪd"); EN_DICT.put("paying", "ˈpeɪɪŋ");
        EN_DICT.put("send", "ˈsɛnd"); EN_DICT.put("sends", "ˈsɛndz");
        EN_DICT.put("sent", "ˈsɛnt"); EN_DICT.put("sending", "ˈsɛndɪŋ");
        EN_DICT.put("keep", "ˈkiːp"); EN_DICT.put("keeps", "ˈkiːpz");
        EN_DICT.put("kept", "ˈkɛpt"); EN_DICT.put("keeping", "ˈkiːpɪŋ");
        EN_DICT.put("let", "ˈlɛt"); EN_DICT.put("lets", "ˈlɛts");
        EN_DICT.put("leave", "ˈliːv"); EN_DICT.put("leaves", "ˈliːvz");
        EN_DICT.put("left", "ˈlɛft"); EN_DICT.put("leaving", "ˈliːvɪŋ");
        EN_DICT.put("begin", "ˈbɪɡɪn"); EN_DICT.put("begins", "ˈbɪɡɪnz");
        EN_DICT.put("began", "ˈbɪɡæn"); EN_DICT.put("begun", "ˈbɪɡʌn");
        EN_DICT.put("hold", "ˈhoʊld"); EN_DICT.put("holds", "ˈhoʊldz");
        EN_DICT.put("held", "ˈhɛld"); EN_DICT.put("holding", "ˈhoʊldɪŋ");
        EN_DICT.put("bring", "ˈbɹɪŋ"); EN_DICT.put("brings", "ˈbɹɪŋz");
        EN_DICT.put("brought", "ˈbɹɔːt"); EN_DICT.put("bringing", "ˈbɹɪŋɪŋ");
        EN_DICT.put("happen", "ˈhæpən"); EN_DICT.put("happens", "ˈhæpənz");
        EN_DICT.put("happened", "ˈhæpənd"); EN_DICT.put("happening", "ˈhæpənɪŋ");

        // Common words (with stress)
        EN_DICT.put("the", "ˈðə"); EN_DICT.put("a", "ˈə"); EN_DICT.put("an", "ˈæn");
        EN_DICT.put("and", "ˈænd"); EN_DICT.put("or", "ˈɔɹ"); EN_DICT.put("but", "ˈbʌt");
        EN_DICT.put("if", "ˈɪf"); EN_DICT.put("then", "ˈðɛn");
        EN_DICT.put("so", "ˈsoʊ"); EN_DICT.put("as", "ˈæz");
        EN_DICT.put("of", "ˈɒv"); EN_DICT.put("in", "ˈɪn"); EN_DICT.put("on", "ˈɒn");
        EN_DICT.put("at", "ˈæt"); EN_DICT.put("to", "ˈtu"); EN_DICT.put("for", "ˈfɔɹ");
        EN_DICT.put("with", "ˈwɪð"); EN_DICT.put("from", "ˈfɹɒm");
        EN_DICT.put("by", "ˈbaɪ"); EN_DICT.put("about", "ˈəbaʊt");
        EN_DICT.put("into", "ˈɪntu"); EN_DICT.put("through", "ˈθɹu");
        EN_DICT.put("during", "ˈdjʊɹɪŋ"); EN_DICT.put("before", "ˈbɪfɔɹ");
        EN_DICT.put("after", "ˈæftəɹ"); EN_DICT.put("above", "ˈəbʌv");
        EN_DICT.put("below", "ˈbɪloʊ"); EN_DICT.put("between", "ˈbɪtwiːn");
        EN_DICT.put("under", "ˈʌndəɹ"); EN_DICT.put("over", "ˈoʊvəɹ");
        EN_DICT.put("up", "ˈʌp"); EN_DICT.put("down", "ˈdaʊn");
        EN_DICT.put("out", "ˈaʊt"); EN_DICT.put("off", "ˈɒf");
        EN_DICT.put("again", "ˈəɡɛn"); EN_DICT.put("also", "ˈɔːlsoʊ");
        EN_DICT.put("very", "ˈvɛɹi"); EN_DICT.put("just", "ˈʤʌst");
        EN_DICT.put("only", "ˈoʊnli"); EN_DICT.put("still", "ˈstɪl");
        EN_DICT.put("even", "ˈiːvən"); EN_DICT.put("never", "ˈnɛvəɹ");
        EN_DICT.put("always", "ˈɔːlweɪz"); EN_DICT.put("often", "ˈɒfən");
        EN_DICT.put("sometimes", "ˈsʌmtaɪmz"); EN_DICT.put("now", "ˈnaʊ");
        EN_DICT.put("here", "ˈhiːɹ"); EN_DICT.put("there", "ˈðɛɹ");
        EN_DICT.put("yes", "ˈjɛs"); EN_DICT.put("no", "ˈnoʊ");
        EN_DICT.put("not", "ˈnɒt"); EN_DICT.put("please", "ˈpliːz");
        EN_DICT.put("thank", "ˈθæŋk"); EN_DICT.put("thanks", "ˈθæŋks");
        EN_DICT.put("sorry", "ˈsɒɹi"); EN_DICT.put("welcome", "ˈwɛlkəm");
        EN_DICT.put("hello", "ˈhɛloʊ"); EN_DICT.put("hi", "ˈhaɪ");
        EN_DICT.put("goodbye", "ˈɡʊdbaɪ"); EN_DICT.put("bye", "ˈbaɪ");
        EN_DICT.put("okay", "ˈoʊkeɪ"); EN_DICT.put("ok", "ˈoʊkeɪ");
        EN_DICT.put("maybe", "ˈmeɪbi"); EN_DICT.put("perhaps", "pəɹˈhæps");
        EN_DICT.put("really", "ˈɹiːli"); EN_DICT.put("actually", "ˈækʧuəli");
        EN_DICT.put("probably", "ˈpɹɒbəbli");
        EN_DICT.put("everything", "ˈɛvɹiθɪŋ");
        EN_DICT.put("something", "ˈsʌmθɪŋ");
        EN_DICT.put("nothing", "ˈnʌθɪŋ");
        EN_DICT.put("anything", "ˈɛniθɪŋ");
        EN_DICT.put("everyone", "ˈɛvɹiwʌn");
        EN_DICT.put("someone", "ˈsʌmwʌn");
        EN_DICT.put("anyone", "ˈɛniwʌn");
        EN_DICT.put("today", "təˈdeɪ"); EN_DICT.put("tomorrow", "təˈmɒɹoʊ");
        EN_DICT.put("yesterday", "ˈjɛstəɹdeɪ");
        EN_DICT.put("morning", "ˈmɔɹnɪŋ"); EN_DICT.put("afternoon", "ˌæftəɹˈnun");
        EN_DICT.put("evening", "ˈiːvnɪŋ"); EN_DICT.put("night", "ˈnaɪt");
        EN_DICT.put("time", "ˈtaɪm"); EN_DICT.put("year", "ˈjiːɹ");
        EN_DICT.put("day", "ˈdeɪ"); EN_DICT.put("week", "ˈwiːk");
        EN_DICT.put("month", "ˈmʌnθ"); EN_DICT.put("hour", "ˈaʊɹ");
        EN_DICT.put("minute", "ˈmɪnɪt"); EN_DICT.put("second", "ˈsɛkənd");
        EN_DICT.put("man", "ˈmæn"); EN_DICT.put("men", "ˈmɛn");
        EN_DICT.put("woman", "ˈwʊmən"); EN_DICT.put("women", "ˈwɪmɪn");
        EN_DICT.put("child", "ˈʧaɪld"); EN_DICT.put("children", "ˈʧɪldɹən");
        EN_DICT.put("boy", "ˈbɔɪ"); EN_DICT.put("girl", "ˈɡɜɹl");
        EN_DICT.put("baby", "ˈbeɪbi"); EN_DICT.put("person", "ˈpɜɹsən");
        EN_DICT.put("people", "ˈpiːpəl"); EN_DICT.put("friend", "ˈfɹɛnd");
        EN_DICT.put("friends", "ˈfɹɛndz"); EN_DICT.put("family", "ˈfæməli");
        EN_DICT.put("mother", "ˈmʌðəɹ"); EN_DICT.put("father", "ˈfɑːðəɹ");
        EN_DICT.put("mom", "ˈmɒm"); EN_DICT.put("dad", "ˈdæd");
        EN_DICT.put("brother", "ˈbɹʌðəɹ"); EN_DICT.put("sister", "ˈsɪstəɹ");
        EN_DICT.put("son", "ˈsʌn"); EN_DICT.put("daughter", "ˈdɔːtəɹ");
        EN_DICT.put("husband", "ˈhʌzbənd"); EN_DICT.put("wife", "ˈwaɪf");
        EN_DICT.put("home", "ˈhoʊm"); EN_DICT.put("house", "ˈhaʊs");
        EN_DICT.put("room", "ˈɹuːm"); EN_DICT.put("door", "ˈdɔɹ");
        EN_DICT.put("window", "ˈwɪndoʊ"); EN_DICT.put("bed", "ˈbɛd");
        EN_DICT.put("table", "ˈteɪbəl"); EN_DICT.put("chair", "ˈʧɛɹ");
        EN_DICT.put("school", "ˈskuːl"); EN_DICT.put("job", "ˈʤɒb");
        EN_DICT.put("money", "ˈmʌni"); EN_DICT.put("food", "ˈfuːd");
        EN_DICT.put("water", "ˈwɔːtəɹ"); EN_DICT.put("book", "ˈbʊk");
        EN_DICT.put("books", "ˈbʊks"); EN_DICT.put("car", "ˈkɑɹ");
        EN_DICT.put("cars", "ˈkɑɹz"); EN_DICT.put("phone", "ˈfoʊn");
        EN_DICT.put("computer", "kəmˈpjuːtəɹ"); EN_DICT.put("world", "ˈwɜɹld");
        EN_DICT.put("country", "ˈkʌntɹi"); EN_DICT.put("city", "ˈsɪti");
        EN_DICT.put("town", "ˈtaʊn"); EN_DICT.put("street", "ˈstɹiːt");
        EN_DICT.put("road", "ˈɹoʊd"); EN_DICT.put("way", "ˈweɪ");
        EN_DICT.put("place", "ˈpleɪs"); EN_DICT.put("thing", "ˈθɪŋ");
        EN_DICT.put("things", "ˈθɪŋz"); EN_DICT.put("life", "ˈlaɪf");
        EN_DICT.put("death", "ˈdɛθ"); EN_DICT.put("heart", "ˈhɑɹt");
        EN_DICT.put("mind", "ˈmaɪnd"); EN_DICT.put("body", "ˈbɒdi");
        EN_DICT.put("head", "ˈhɛd"); EN_DICT.put("hand", "ˈhænd");
        EN_DICT.put("eye", "ˈaɪ"); EN_DICT.put("eyes", "ˈaɪz");
        EN_DICT.put("face", "ˈfeɪs"); EN_DICT.put("smile", "ˈsmaɪl");
        EN_DICT.put("happy", "ˈhæpi"); EN_DICT.put("sad", "ˈsæd");
        EN_DICT.put("angry", "ˈæŋɡɹi"); EN_DICT.put("afraid", "əˈfɹeɪd");
        EN_DICT.put("good", "ˈɡʊd"); EN_DICT.put("bad", "ˈbæd");
        EN_DICT.put("great", "ˈɡɹeɪt"); EN_DICT.put("nice", "ˈnaɪs");
        EN_DICT.put("beautiful", "ˈbjuːtɪfəl"); EN_DICT.put("ugly", "ˈʌɡli");
        EN_DICT.put("big", "ˈbɪɡ"); EN_DICT.put("small", "ˈsmɔːl");
        EN_DICT.put("long", "ˈlɒŋ"); EN_DICT.put("short", "ˈʃɔɹt");
        EN_DICT.put("new", "ˈnjuː"); EN_DICT.put("old", "ˈoʊld");
        EN_DICT.put("young", "ˈjʌŋ"); EN_DICT.put("first", "ˈfɜɹst");
        EN_DICT.put("last", "ˈlæst"); EN_DICT.put("next", "ˈnɛkst");
        EN_DICT.put("right", "ˈɹaɪt"); EN_DICT.put("left", "ˈlɛft");
        EN_DICT.put("high", "ˈhaɪ"); EN_DICT.put("low", "ˈloʊ");
        EN_DICT.put("hot", "ˈhɒt"); EN_DICT.put("cold", "ˈkoʊld");
        EN_DICT.put("warm", "ˈwɔɹm"); EN_DICT.put("cool", "ˈkuːl");
        EN_DICT.put("fast", "ˈfæst"); EN_DICT.put("slow", "ˈsloʊ");
        EN_DICT.put("hard", "ˈhɑɹd"); EN_DICT.put("easy", "ˈiːzi");
        EN_DICT.put("true", "ˈtɹuː"); EN_DICT.put("false", "ˈfɔːls");
        EN_DICT.put("real", "ˈɹiːəl"); EN_DICT.put("important", "ɪmˈpɔɹtənt");
        EN_DICT.put("different", "ˈdɪfɹənt"); EN_DICT.put("same", "ˈseɪm");
        EN_DICT.put("special", "ˈspɛʃəl"); EN_DICT.put("possible", "ˈpɒsɪbəl");
        EN_DICT.put("white", "ˈwaɪt"); EN_DICT.put("black", "ˈblæk");
        EN_DICT.put("red", "ˈɹɛd"); EN_DICT.put("blue", "ˈbluː");
        EN_DICT.put("green", "ˈɡɹiːn"); EN_DICT.put("yellow", "ˈjɛloʊ");
        EN_DICT.put("one", "ˈwʌn"); EN_DICT.put("two", "ˈtuː");
        EN_DICT.put("three", "ˈθɹiː"); EN_DICT.put("four", "ˈfɔɹ");
        EN_DICT.put("five", "ˈfaɪv"); EN_DICT.put("six", "ˈsɪks");
        EN_DICT.put("seven", "ˈsɛvən"); EN_DICT.put("eight", "ˈeɪt");
        EN_DICT.put("nine", "ˈnaɪn"); EN_DICT.put("ten", "ˈtɛn");
        EN_DICT.put("hundred", "ˈhʌndɹəd"); EN_DICT.put("thousand", "ˈθaʊzənd");
        EN_DICT.put("million", "ˈmɪljən");
        EN_DICT.put("test", "ˈtɛst"); EN_DICT.put("tests", "ˈtɛsts");
        EN_DICT.put("speech", "ˈspiʧ"); EN_DICT.put("text", "ˈtɛkst");
        EN_DICT.put("voice", "ˈvɔɪs"); EN_DICT.put("audio", "ˈɔːdioʊ");
        EN_DICT.put("sound", "ˈsaʊnd"); EN_DICT.put("music", "ˈmjuːzɪk");
        EN_DICT.put("word", "ˈwɜɹd"); EN_DICT.put("words", "ˈwɜɹdz");
        EN_DICT.put("language", "ˈlæŋɡwɪʤ"); EN_DICT.put("english", "ˈɪŋɡlɪʃ");
        EN_DICT.put("kokoro", "ˈkoʊkoʊɹoʊ");

        // Abbreviations
        ABBR_EN.put("mr", "ˈmɪstəɹ"); ABBR_EN.put("mrs", "ˈmɪsɪz");
        ABBR_EN.put("ms", "ˈmɪz"); ABBR_EN.put("dr", "ˈdɒktəɹ");
        ABBR_EN.put("prof", "pɹəˈfɛsəɹ"); ABBR_EN.put("st", "seɪnt");
        ABBR_EN.put("jr", "ˈʤuːniəɹ"); ABBR_EN.put("sr", "ˈsiːniəɹ");
        ABBR_EN.put("vs", "ˈvɜɹsəs"); ABBR_EN.put("etc", "ɛtˈsɛtəɹə");

        // ============ HINDI WITH STRESS ============
        HI_DICT.put("नमस्ते", "nəməˈsteː");
        HI_DICT.put("नमस्कार", "nəməˈskɑːɹ");
        HI_DICT.put("धन्यवाद", "d̪ʱənjiəˈʋɑːd̪");
        HI_DICT.put("शुक्रिया", "ʃʊkˈɹiə");
        HI_DICT.put("हाँ", "ˈɦɑ̃ː"); HI_DICT.put("हां", "ˈɦɑ̃ː");
        HI_DICT.put("नहीं", "nəˈɦĩː"); HI_DICT.put("नही", "nəˈɦiː");
        HI_DICT.put("जी", "ˈʤiː");
        HI_DICT.put("अच्छा", "ˈətʃʰɑː");
        HI_DICT.put("ठीक", "ˈt̪ʰiːk");
        HI_DICT.put("सही", "səˈɦiː");
        HI_DICT.put("गलत", "ɡəˈlət̪");
        HI_DICT.put("मैं", "ˈmɛ̃ː");
        HI_DICT.put("तू", "ˈt̪uː"); HI_DICT.put("तुम", "ˈt̪ʊm"); HI_DICT.put("आप", "ˈɑːp");
        HI_DICT.put("हम", "ˈɦəm");
        HI_DICT.put("वह", "ˈʋəɦ"); HI_DICT.put("वो", "ˈʋoː"); HI_DICT.put("वे", "ˈʋeː");
        HI_DICT.put("यह", "ˈjəɦ"); HI_DICT.put("ये", "ˈjeː");
        HI_DICT.put("इस", "ˈɪs"); HI_DICT.put("उस", "ˈʊs");
        HI_DICT.put("क्या", "ˈkjɑː");
        HI_DICT.put("कैसे", "ˈkɛːseː");
        HI_DICT.put("कब", "ˈkəb");
        HI_DICT.put("कहाँ", "kəˈɦɑ̃ː"); HI_DICT.put("कहां", "kəˈɦɑ̃ː");
        HI_DICT.put("क्यों", "ˈkjõː");
        HI_DICT.put("कौन", "ˈkɔːn");
        HI_DICT.put("है", "ˈhɛː"); HI_DICT.put("हैं", "ˈhɛ̃ː");
        HI_DICT.put("था", "ˈt̪ʰɑː"); HI_DICT.put("थी", "ˈt̪ʰiː"); HI_DICT.put("थे", "ˈt̪ʰeː");
        HI_DICT.put("हूँ", "ˈɦũː"); HI_DICT.put("हो", "ˈɦoː");
        HI_DICT.put("होगा", "ɦoːˈɡɑː"); HI_DICT.put("होगी", "ɦoːˈɡiː");
        HI_DICT.put("कर", "ˈkəɹ"); HI_DICT.put("करना", "kəɹˈnɑː");
        HI_DICT.put("किया", "ˈkiə");
        HI_DICT.put("जा", "ˈʤɑː"); HI_DICT.put("जाना", "ʤɑːˈnɑː");
        HI_DICT.put("गया", "ɡəˈjɑː"); HI_DICT.put("गई", "ɡəˈjiː");
        HI_DICT.put("आ", "ˈɑː"); HI_DICT.put("आना", "ɑːˈnɑː");
        HI_DICT.put("आया", "ˈɑːjɑː");
        HI_DICT.put("दे", "ˈd̪eː"); HI_DICT.put("देना", "d̪eːˈnɑː");
        HI_DICT.put("दिया", "ˈd̪iə");
        HI_DICT.put("ले", "ˈleː"); HI_DICT.put("लेना", "leːˈnɑː");
        HI_DICT.put("लिया", "ˈliə");
        HI_DICT.put("बोल", "ˈboːl"); HI_DICT.put("बोलना", "boːlˈnɑː");
        HI_DICT.put("सुन", "ˈsʊn"); HI_DICT.put("सुनना", "sʊnˈnɑː");
        HI_DICT.put("देख", "ˈd̪eːkʰ"); HI_DICT.put("देखना", "d̪eːkʰˈnɑː");
        HI_DICT.put("खा", "ˈkʰɑː"); HI_DICT.put("खाना", "kʰɑːˈnɑː");
        HI_DICT.put("पी", "ˈpiː"); HI_DICT.put("पीना", "piːˈnɑː");
        HI_DICT.put("भारत", "ˈbʱɑːɹət̪");
        HI_DICT.put("हिंदी", "ˈɦind̪iː"); HI_DICT.put("हिन्दी", "ˈɦind̪iː");
        HI_DICT.put("दुनिया", "d̪ʊˈniə");
        HI_DICT.put("ज़िंदगी", "zind̪ˈɡiː");
        HI_DICT.put("प्यार", "ˈpjɑːɹ");
        HI_DICT.put("दोस्त", "ˈd̪oːst̪");
        HI_DICT.put("परिवार", "pəɹiˈʋɑːɹ");
        HI_DICT.put("घर", "ˈɡʱəɹ");
        HI_DICT.put("पानी", "ˈpɑːniː");
        HI_DICT.put("रोटी", "ˈɹoːʈiː");
        HI_DICT.put("चाय", "ˈʧɑːeː");
        HI_DICT.put("किताब", "kiˈt̪ɑːb");
        HI_DICT.put("काम", "ˈkɑːm");
        HI_DICT.put("पैसा", "ˈpɛːsɑː");
        HI_DICT.put("समय", "səˈməeː");
        HI_DICT.put("दिन", "ˈd̪in");
        HI_DICT.put("रात", "ˈɹɑːt̪");
        HI_DICT.put("सुबह", "ˈsʊbəɦ");
        HI_DICT.put("शाम", "ˈʃɑːm");
        HI_DICT.put("आज", "ˈɑːʤ");
        HI_DICT.put("कल", "ˈkəl");
        HI_DICT.put("एक", "ˈeːk"); HI_DICT.put("दो", "ˈd̪oː");
        HI_DICT.put("तीन", "ˈt̪iːn"); HI_DICT.put("चार", "ˈʧɑːɹ");
        HI_DICT.put("पांच", "ˈpɑːnʧ"); HI_DICT.put("सात", "ˈsɑːt̪");
        HI_DICT.put("आठ", "ˈɑːʈʰ"); HI_DICT.put("नौ", "ˈnɔː");
        HI_DICT.put("दस", "ˈd̪əs");

        // Numbers
        NUM_EN.put("0", "ˈziːɹoʊ"); NUM_EN.put("1", "ˈwʌn");
        NUM_EN.put("2", "ˈtuː"); NUM_EN.put("3", "ˈθɹiː");
        NUM_EN.put("4", "ˈfɔɹ"); NUM_EN.put("5", "ˈfaɪv");
        NUM_EN.put("6", "ˈsɪks"); NUM_EN.put("7", "ˈsɛvən");
        NUM_EN.put("8", "ˈeɪt"); NUM_EN.put("9", "ˈnaɪn");
        NUM_EN.put("10", "ˈtɛn"); NUM_EN.put("11", "ɪˈlɛvən");
        NUM_EN.put("12", "ˈtwɛlv"); NUM_EN.put("20", "ˈtwɛnti");
        NUM_EN.put("100", "ˈhʌndɹəd"); NUM_EN.put("1000", "ˈθaʊzənd");

        NUM_HI.put("०", "ˈʃʊnjiə"); NUM_HI.put("१", "ˈeːk");
        NUM_HI.put("२", "ˈd̪oː"); NUM_HI.put("३", "ˈt̪iːn");
        NUM_HI.put("४", "ˈʧɑːɹ"); NUM_HI.put("५", "ˈpɑːnʧ");
        NUM_HI.put("६", "ˈʧʰəɦ"); NUM_HI.put("७", "ˈsɑːt̪");
        NUM_HI.put("८", "ˈɑːʈʰ"); NUM_HI.put("९", "ˈnɔː");
    }

    public static String phonemize(String text, String voiceId) {
        if (text == null || text.trim().isEmpty()) return "";
        text = text.trim();

        if (voiceId.startsWith("am_") || voiceId.startsWith("bf_") ||
            voiceId.startsWith("bm_") || voiceId.startsWith("af_") ||
            voiceId.startsWith("ef_") || voiceId.startsWith("em_")) {
            return phonemizeEnglish(text);
        } else if (voiceId.startsWith("hm_") || voiceId.startsWith("hf_")) {
            return phonemizeHindi(text);
        } else {
            return phonemizeEnglish(text);
        }
    }

    private static String phonemizeEnglish(String text) {
        text = text.toLowerCase();
        // Handle abbreviations
        for (Map.Entry<String, String> entry : ABBR_EN.entrySet()) {
            text = text.replaceAll("\\b" + entry.getKey() + "\\.", entry.getValue());
        }
        // Normalize punctuation
        text = text.replace(",", " , ").replace(".", " . ");
        text = text.replace("!", " ! ").replace("?", " ? ");
        text = text.replace(";", " ; ").replace(":", " : ");
        text = text.replace("-", " ");
        text = text.replaceAll("\\s+", " ").trim();

        StringBuilder result = new StringBuilder();
        String[] tokens = text.split("\\s+");
        boolean first = true;
        boolean isQuestion = text.trim().endsWith("?");

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

        // Add prosody: questions get rising intonation marker
        if (isQuestion && result.length() > 0) {
            result.append(" ˈ");  // Stress at end for rising intonation
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
