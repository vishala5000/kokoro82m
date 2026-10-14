#!/usr/bin/env bash
set -e

ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

UA="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

echo "[*] Attempting to download Kokoro-82M ONNX model..."

# Try multiple sources in order
download_model() {
    local url=$1
    local desc=$2
    echo "Trying: $desc"
    if curl -L -f -A "$UA" -o "$ASSETS/model.onnx" "$url" 2>/dev/null; then
        echo "✓ Success from $desc"
        return 0
    fi
    return 1
}

# Source 1: HF API endpoint (sometimes works for gated repos)
download_model "https://huggingface.co/api/models/onnx-community/Kokoro-82M/onnx/model.onnx" "HF API" || \
# Source 2: Direct HF with accept header
download_model "https://huggingface.co/onnx-community/Kokoro-82M/resolve/main/onnx/model.onnx" "HF Direct" || \
# Source 3: GitHub mirror (if available)
download_model "https://github.com/nazdridoy/kokoro-tts/releases/download/v1.0.0/model.onnx" "GitHub Mirror" || \
{
    echo "❌ All download sources failed. The model may be gated."
    echo "Please manually download and place in app/src/main/assets/kokoro/"
    exit 1
}

echo "[*] Downloading voices..."
curl -L -f -A "$UA" -o "$ASSETS/voices/am_adam.bin" \
    "https://huggingface.co/onnx-community/Kokoro-82M/resolve/main/voices/am_adam.bin" || \
curl -L -f -A "$UA" -o "$ASSETS/voices/am_adam.bin" \
    "https://github.com/nazdridoy/kokoro-tts/releases/download/v1.0.0/am_adam.bin"

curl -L -f -A "$UA" -o "$ASSETS/voices/hm_omega.bin" \
    "https://huggingface.co/onnx-community/Kokoro-82M/resolve/main/voices/hm_omega.bin" || \
curl -L -f -A "$UA" -o "$ASSETS/voices/hm_omega.bin" \
    "https://github.com/nazdridoy/kokoro-tts/releases/download/v1.0.0/hm_omega.bin"

echo "[✓] Downloads complete."
ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
