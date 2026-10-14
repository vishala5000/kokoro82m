package com.example.kokorotts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Spinner voiceSpinner;
    private SeekBar speedSlider;
    private TextView speedLabel;
    private Button btnGenerate, btnPlay, btnSave;
    private ProgressBar progress;
    private TextView status;

    private KokoroEngine engine;
    private float[] lastAudio;
    private String lastVoiceId;

    private static final int SAMPLE_RATE = 24000;
    private static final String[] VOICE_IDS = {"am_adam", "hm_omega"};
    private static final String[] VOICE_LANGS = {"a", "h"};

    private float currentSpeed = 1.0f;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        inputText    = findViewById(R.id.inputText);
        voiceSpinner = findViewById(R.id.voiceSpinner);
        speedSlider  = findViewById(R.id.speedSlider);
        speedLabel   = findViewById(R.id.speedLabel);
        btnGenerate  = findViewById(R.id.btnGenerate);
        btnPlay      = findViewById(R.id.btnPlay);
        btnSave      = findViewById(R.id.btnSave);
        progress     = findViewById(R.id.progress);
        status       = findViewById(R.id.status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.voices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        voiceSpinner.setAdapter(adapter);

        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentSpeed = progress / 10.0f;
                speedLabel.setText(String.format("%.1fx", currentSpeed));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        requestAudioPermission();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                engine = new KokoroEngine(getApplicationContext());
                runOnUiThread(() -> status.setText("Ready. Kokoro 82M Loaded."));
            } catch (Throwable t) {
                // FIXED: Print full error details to Logcat and screen
                Log.e("KokoroTTS", "Init failed", t);
                String errorMsg = t.getMessage() != null ? t.getMessage() : t.toString();
                runOnUiThread(() -> status.setText("Init failed: " + errorMsg));
            }
        });

        btnGenerate.setOnClickListener(v -> onGenerate());
        btnPlay.setOnClickListener(v -> onPlay());
        btnSave.setOnClickListener(v -> onSave());
    }

    private void onGenerate() {
        String text = inputText.getText().toString().trim();
        if (text.isEmpty()) { toast("Enter some text"); return; }
        if (engine == null) { toast("Engine still loading or failed"); return; }

        int idx = voiceSpinner.getSelectedItemPosition();
        String voiceId = VOICE_IDS[idx];
        String lang    = VOICE_LANGS[idx];

        btnGenerate.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        status.setText("Synthesizing...");

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                long t0 = System.currentTimeMillis();
                float[] audio = engine.synthesize(text, voiceId, lang, currentSpeed);
                long ms = System.currentTimeMillis() - t0;
                lastAudio = audio;
                lastVoiceId = voiceId;

                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    btnPlay.setEnabled(true);
                    btnSave.setEnabled(true);
                    status.setText(String.format(
                        "Done in %d ms — %.2fs audio @ %d Hz",
                        ms, audio.length / (float)SAMPLE_RATE, SAMPLE_RATE));
                });
            } catch (Throwable t) {
                Log.e("KokoroTTS", "Synthesis failed", t);
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    status.setText("Error: " + t.getMessage());
                });
            }
        });
    }

    private void onPlay() {
        if (lastAudio == null) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                short[] pcm = AudioUtils.floatToPcm16(lastAudio);
                AudioTrack track = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        pcm.length * 2,
                        AudioTrack.MODE_STREAM);
                track.play();
                track.write(pcm, 0, pcm.length);
                track.stop();
                track.release();
            } catch (Throwable t) {
                runOnUiThread(() -> toast("Play error: " + t.getMessage()));
            }
        });
    }

    private void onSave() {
        if (lastAudio == null) return;
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestAudioPermission();
                return;
            }
        }
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC + "/KokoroTTS");
            if (!dir.exists()) dir.mkdirs();
            String name = "kokoro_" + lastVoiceId + "_" + System.currentTimeMillis() + ".wav";
            File out = new File(dir, name);

            try (OutputStream os = new FileOutputStream(out)) {
                byte[] wav = AudioUtils.toWav(lastAudio, SAMPLE_RATE);
                os.write(wav);
            }
            toast("Saved: " + out.getAbsolutePath());
        } catch (Throwable t) {
            toast("Save error: " + t.getMessage());
        }
    }

    private void requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 100);
        } else if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int req, @NonNull String[] perm,
                                           @NonNull int[] res) {
        super.onRequestPermissionsResult(req, perm, res);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
