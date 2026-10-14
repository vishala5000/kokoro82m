package com.example.kokorotts;

import android.content.Context;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Real BPE (Byte-Pair Encoding) tokenizer for Kokoro-82M.
 * Loads tokenizer.json from assets and produces the exact token IDs
 * that the ONNX model expects. This gives studio-quality pronunciation.
 */
public class KokoroTokenizer {

    private final Map<String, Integer> vocab = new HashMap<>();
    private final Map<String, Integer> mergeRanks = new HashMap<>();
    
    // Special token IDs
    private static final int PAD_ID = 0;
    private static final int UNK_ID = 1;
    
    // Pre-tokenization pattern: splits on whitespace and punctuation
    private static final Pattern PRE_TOKENIZE_PATTERN = 
        Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+");

    public KokoroTokenizer(Context ctx) throws Exception {
        File tokenizerFile = new File(ctx.getFilesDir(), "kokoro/tokenizer.json");
        if (!tokenizerFile.exists()) {
            copyAsset(ctx, "tokenizer.json", tokenizerFile);
        }
        
        // Parse tokenizer.json
        String jsonStr = readFile(tokenizerFile);
        JSONObject root = new JSONObject(jsonStr);
        
        // Load vocabulary
        JSONObject modelObj = root.getJSONObject("model");
        JSONObject vocabObj = modelObj.getJSONObject("vocab");
        
        for (java.util.Iterator<String> it = vocabObj.keys(); it.hasNext(); ) {
            String token = it.next();
            // Kokoro uses unicode escapes like "\u0251" for IPA symbols
            String decodedToken = decodeToken(token);
            int id = vocabObj.getInt(token);
            vocab.put(decodedToken, id);
        }
        
        // Load merge rules with ranks
        JSONArray mergesArray = modelObj.getJSONArray("merges");
        for (int i = 0; i < mergesArray.length(); i++) {
            String merge = mergesArray.getString(i);
            // Format: "first second" (space-separated pair)
            mergeRanks.put(merge, i);
        }
    }

    /**
     * Encode text into token IDs using full BPE algorithm.
     */
    public long[] encode(String text) {
        if (text == null || text.isEmpty()) {
            return new long[]{PAD_ID};
        }
        
        // 1. Normalize: lowercase and strip
        text = text.toLowerCase().trim();
        
        // 2. Pre-tokenize into words
        List<String> preTokens = preTokenize(text);
        
        // 3. Apply BPE to each pre-token
        List<Long> allIds = new ArrayList<>();
        for (String preToken : preTokens) {
            if (preToken.isEmpty()) continue;
            
            List<String> bpeTokens = bpe(preToken);
            for (String token : bpeTokens) {
                Integer id = vocab.get(token);
                if (id != null) {
                    allIds.add(id.longValue());
                } else {
                    // Fallback: try character by character
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

    /**
     * Pre-tokenize text into words using GPT-2 style regex.
     */
    private List<String> preTokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = PRE_TOKENIZE_PATTERN.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    /**
     * Apply BPE merge rules to a single pre-token.
     */
    private List<String> bpe(String token) {
        // Start with individual characters
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < token.length(); i++) {
            String ch = String.valueOf(token.charAt(i));
            if (i == token.length() - 1) {
                ch += "</w>";  // End-of-word marker
            }
            symbols.add(ch);
        }
        
        // Iteratively merge pairs
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
            
            if (bestPair == null) break;  // No more merges possible
            
            // Merge the best pair
            String[] parts = bestPair.split(" ", 2);
            String merged = parts[0] + parts[1];
            symbols.set(bestIndex, merged);
            symbols.remove(bestIndex + 1);
        }
        
        return symbols;
    }

    /**
     * Decode unicode escape sequences in token strings.
     * HuggingFace tokenizer.json uses "\u0251" format for IPA symbols.
     */
    private String decodeToken(String token) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c == '\\' && i + 1 < token.length() && token.charAt(i + 1) == 'u') {
                // Unicode escape: \uXXXX
                if (i + 5 < token.length()) {
                    String hex = token.substring(i + 2, i + 6);
                    try {
                        int codePoint = Integer.parseInt(hex, 16);
                        result.appendCodePoint(codePoint);
                        i += 5;
                        continue;
                    } catch (NumberFormatException e) {
                        // Not a valid escape, keep as-is
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
