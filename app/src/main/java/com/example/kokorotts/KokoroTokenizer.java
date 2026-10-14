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
    
    private int padTokenId;
    private int unkTokenId;
    
    // GPT-2 style pre-tokenization pattern
    private static final Pattern PRE_TOKENIZE_PATTERN = 
        Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+");

    public KokoroTokenizer(Context ctx) throws Exception {
        File tokenizerFile = new File(ctx.getFilesDir(), "kokoro/tokenizer.json");
        File configfile = new File(ctx.getFilesDir(), "kokoro/tokenizer_config.json");
        
        // Copy from assets if not exists
        if (!tokenizerFile.exists()) {
            Log.d("KokoroTTS", "Copying tokenizer.json from assets...");
            copyAsset(ctx, "kokoro/tokenizer.json", tokenizerFile);
        }
        
        if (!configfile.exists()) {
            Log.d("KokoroTTS", "Copying tokenizer_config.json from assets...");
            copyAsset(ctx, "kokoro/tokenizer_config.json", configfile);
        }
        
        if (!tokenizerFile.exists() || tokenizerFile.length() < 100) {
            throw new RuntimeException("tokenizer.json is missing or corrupted");
        }
        
        Log.d("KokoroTTS", "Loading tokenizer config...");
        String configStr = readFile(configfile);
        JSONObject config = new JSONObject(configStr);
        
        // Get PAD and UNK tokens from config (both are "$" in Kokoro)
        String padToken = config.getString("pad_token");
        String unkToken = config.getString("unk_token");
        Log.d("KokoroTTS", "PAD token: " + padToken + ", UNK token: " + unkToken);
        
        Log.d("KokoroTTS", "Loading tokenizer vocabulary...");
        String jsonStr = readFile(tokenizerFile);
        JSONObject root = new JSONObject(jsonStr);
        
        // Load vocabulary
        JSONObject modelObj = root.getJSONObject("model");
        JSONObject vocabObj = modelObj.getJSONObject("vocab");
        
        for (java.util.Iterator<String> it = vocabObj.keys(); it.hasNext(); ) {
            String token = it.next();
            String decodedToken = decodeUnicodeEscapes(token);
            int id = vocabObj.getInt(token);
            vocab.put(decodedToken, id);
            
            // Set PAD and UNK token IDs
            if (decodedToken.equals(padToken)) {
                padTokenId = id;
                Log.d("KokoroTTS", "PAD token ID: " + padTokenId);
            }
            if (decodedToken.equals(unkToken)) {
                unkTokenId = id;
                Log.d("KokoroTTS", "UNK token ID: " + unkTokenId);
            }
        }
        
        // Load merge rules
        JSONArray mergesArray = modelObj.getJSONArray("merges");
        for (int i = 0; i < mergesArray.length(); i++) {
            String merge = mergesArray.getString(i);
            mergeRanks.put(merge, i);
        }
        
        Log.d("KokoroTTS", "Tokenizer loaded. Vocab size: " + vocab.size() + ", Merges: " + mergeRanks.size());
    }

    public long[] encode(String text) {
        if (text == null || text.isEmpty()) {
            return new long[]{padTokenId};
        }
        
        // Pre-tokenize
        List<String> preTokens = preTokenize(text);
        
        // Apply BPE to each token
        List<Long> allIds = new ArrayList<>();
        for (String preToken : preTokens) {
            if (preToken.isEmpty()) continue;
            
            List<String> bpeTokens = bpe(preToken);
            for (String token : bpeTokens) {
                Integer id = vocab.get(token);
                if (id != null) {
                    allIds.add(id.longValue());
                } else {
                    // Use UNK token for unknown tokens
                    allIds.add((long) unkTokenId);
                }
            }
        }
        
        if (allIds.isEmpty()) {
            return new long[]{padTokenId};
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
        // Start with individual characters + end-of-word marker
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < token.length(); i++) {
            String ch = String.valueOf(token.charAt(i));
            if (i == token.length() - 1) {
                ch += "</w>";
            }
            symbols.add(ch);
        }
        
        // Iteratively merge pairs based on merge ranks
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

    private String decodeUnicodeEscapes(String token) {
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
                        // Not a valid escape
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
