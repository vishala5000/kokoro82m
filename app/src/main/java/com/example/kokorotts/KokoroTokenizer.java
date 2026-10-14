package com.example.kokorotts;

import android.content.Context;
import android.util.Log;

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

public class KokoroTokenizer {

    private final Map<Character, Integer> vocab = new HashMap<>();
    private int padTokenId = 0;  // '$' = 0
    
    public KokoroTokenizer(Context ctx) throws Exception {
        File tokenizerFile = new File(ctx.getFilesDir(), "kokoro/tokenizer.json");
        
        if (!tokenizerFile.exists()) {
            Log.d("KokoroTTS", "Copying tokenizer.json from assets...");
            copyAsset(ctx, "kokoro/tokenizer.json", tokenizerFile);
        }
        
        if (!tokenizerFile.exists() || tokenizerFile.length() < 100) {
            throw new RuntimeException("tokenizer.json is missing or corrupted");
        }
        
        Log.d("KokoroTTS", "Loading Kokoro tokenizer...");
        String jsonStr = readFile(tokenizerFile);
        JSONObject root = new JSONObject(jsonStr);
        
        // Load vocabulary (character -> ID)
        JSONObject vocabObj = root.getJSONObject("model").getJSONObject("vocab");
        for (java.util.Iterator<String> it = vocabObj.keys(); it.hasNext(); ) {
            String token = it.next();
            String decoded = decodeUnicodeEscapes(token);
            int id = vocabObj.getInt(token);
            if (decoded.length() == 1) {
                vocab.put(decoded.charAt(0), id);
            }
        }
        
        Log.d("KokoroTTS", "Tokenizer loaded. Vocab size: " + vocab.size());
    }

    /**
     * Encode IPA phoneme string into token IDs.
     * Kokoro's real behavior:
     *   1. Strip characters not in vocab
     *   2. Map each character to its ID
     *   3. Wrap with $ (ID 0) on both sides
     */
    public long[] encode(String text) {
        if (text == null || text.isEmpty()) {
            return new long[]{padTokenId};
        }
        
        List<Long> ids = new ArrayList<>();
        
        // Leading $ (post-processor adds this)
        ids.add((long) padTokenId);
        
        // Map each character
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer id = vocab.get(c);
            if (id != null) {
                ids.add(id.longValue());
            }
            // Characters not in vocab are silently dropped (matches Kokoro's normalizer)
        }
        
        // Trailing $ (post-processor adds this)
        ids.add((long) padTokenId);
        
        if (ids.size() <= 2) {
            // Only the two $ tokens, nothing useful
            return new long[]{padTokenId};
        }
        
        long[] result = new long[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            result[i] = ids.get(i);
        }
        return result;
    }

    private String decodeUnicodeEscapes(String token) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c == '\\' && i + 5 < token.length() && token.charAt(i + 1) == 'u') {
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
