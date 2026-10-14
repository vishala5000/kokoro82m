// Add these fields at the top of the class
private SeekBar speedSlider;
private TextView speedLabel;
private float currentSpeed = 1.0f;

// In onCreate(), after setting up the spinner, add:
speedSlider = findViewById(R.id.speedSlider);
speedLabel = findViewById(R.id.speedLabel);

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

// In onGenerate(), change this line:
float[] audio = engine.synthesize(text, voiceId, lang, currentSpeed);
