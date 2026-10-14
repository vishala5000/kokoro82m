package com.example.kokorotts;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KokoroTokenizer {

    private final Map<String, Integer> vocab = new HashMap<>();
    private final Map<String, Integer> mergeRanks = new HashMap<>();
    
    private static final int PAD_ID = 0;
    private static final int UNK_ID = 1;
    
    private static final Pattern PRE_TOKENIZE_PATTERN = 
        Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+");

    public KokoroTokenizer(Context ctx) throws Exception {
        File tokenizerFile = new File(ctx.getFilesDir(), "kokoro/tokenizer.json");
        
        // FIXED: Robust asset copying with explicit error checking
        if (!tokenizerFile.exists()) {
            Log.d("KokoroTTS", "Tokenizer not in internal storage, copying from assets...");
            copyAsset(ctx, "kokoro/tokenizer.json", tokenizerFile);
        }
        
        if (!tokenizerFile.exists() || tokenizerFile.length() == 0) {
            throw new RuntimeException("tokenizer.json is missing or empty in assets. Did you add it to your GitHub Release?");
        }
        
        Log.d("KokoroTTS", "Loading tokenizer from: " + tokenizerFile.getAbsolutePath() + " (Size: " + tokenizerFile.length() + " bytes)");
        String jsonStr = readFile(tokenizerFile);
        
        try {
            JSONObject root = new JSONObject(jsonStr);
            JSONObject modelObj = root.getJSONObject("model");
            JSONObject vocabObj = modelObj.getJSONObject("vocab");
            
            for (java.util.Iterator<String> it = vocabObj.keys(); it.hasNext(); ) {
                String token = it.next();
                String decodedToken = decodeToken(token);
                int id = vocabObj.getInt(token);
                vocab.put(decodedToken, id);
            }
            
            JSONArray mergesArray = modelObj.getJSONArray("merges");
            for (int i = 0; i < mergesArray.length(); i++) {
                String merge = mergesArray.getString(i);
                mergeRanks.put(merge, i);
            }
            Log.d("KokoroTTS", "Tokenizer loaded successfully. Vocab size: " + vocab.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse tokenizer.json. It may be corrupted or an HTML error page. Error: " + e.getMessage(), e);
        }
    }

    public long[] encode(String text) {
        if (text == null || text.isEmpty()) {
            return new long[]{PAD_ID};
        }
        
        text = text.toLowerCase().trim();
        List<String> preTokens = preTokenize(text);
        
        List<Long> allIds = new ArrayList<>();
        for (String preToken : preTokens) {
            if (preToken.isEmpty()) continue;
            
            List<String> bpeTokens = bpe(preToken);
            for (String token : bpeTokens) {
                Integer id = vocab.get(token);
                if (id != null) {
                    allIds.add(id.longValue());
                } else {
                    for (char c : token.toCharArray()) {
                        Integer charId = vocab.get(String.valueOf(c));
                        allIds.add(charId != null ? charId.longValue() : UNK_ID);
                    }
                }
            }
        }
        
        if (allIds.isEmpty()) {
            return new long[]{PAD_ID};
        }
        
        long[] result = new long[allIds.size()];
        for (int i = 0; i < allIds.size(); i++) {
            result[i] = allIds.get(i);
        }
        return result;
    }

    private List<String> preTokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = PRE_TOKENIZE_PATTERN.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    private List<String> bpe(String token) {
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < token.length(); i++) {
            String ch = String.valueOf(token.charAt(i));
            if (i == token.length() - 1) {
                ch += "</w>";
            }
            symbols.add(ch);
        }
        
        while (true) {
            String bestPair = null;
            int bestRank = Integer.MAX_VALUE;
            int bestIndex = -1;
            
            for (int i = 0; i < symbols.size() - 1; i++) {
                String pair = symbols.get(i) + " " + symbols.get(i + 1);
                Integer rank = mergeRanks.get(pair);
                if (rank != null && rank < bestRank) {
                    bestRank = rank;
                    bestPair = pair;
                    bestIndex = i;
                }
            }
            
            if (bestPair == null) break;
            
            String[] parts = bestPair.split(" ", 2);
            String merged = parts[0] + parts[1];
            symbols.set(bestIndex, merged);
            symbols.remove(bestIndex + 1);
        }
        
        return symbols;
    }

    private String decodeToken(String token) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c == '\\' && i + 1 < token.length() && token.charAt(i + 1) == 'u') {
                if (i + 5 < token.length()) {
                    String hex = token.substring(i + 2, i + 6);
                    try {
                        int codePoint = Integer.parseInt(hex, 16);
                        result.appendCodePoint(codePoint);
                        i += 5;
                        continue;
                    } catch (NumberFormatException e) {
                        // Ignore invalid escapes
                    }
                }
            }
            result.append(c);
        }
        return result.toString();
    }

    private void copyAsset(Context ctx, String asset, File dst) throws Exception {
        dst.getParentFile().mkdirs();
        try (InputStream is = ctx.getAssets().open(asset);
             FileOutputStream os = new FileOutputStream(dst)) {
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = is.read(buf)) > 0) os.write(buf, 0, n);
        }
    }

    private String readFile(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
