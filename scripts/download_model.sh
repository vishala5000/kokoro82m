#!/usr/bin/env bash
set -e

REPO="onnx-community/Kokoro-82M"
BASE="https://huggingface.co/${REPO}/resolve/main"
ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

echo "[*] Downloading Kokoro-82M ONNX fp16 model..."
# Note: The model is inside the 'onnx' subfolder
wget -q --show-progress -O "$ASSETS/model.onnx" \
  "${BASE}/onnx/model.onnx?download=true"

echo "[*] Downloading voices (am_adam, hm_omega)..."
wget -q --show-progress -O "$ASSETS/voices/am_adam.bin" \
  "${BASE}/voices/am_adam.bin?download=true"
wget -q --show-progress -O "$ASSETS/voices/hm_omega.bin" \
  "${BASE}/voices/hm_omega.bin?download=true"

echo "[*] Downloading tokenizer (optional fallback)..."
wget -q --show-progress -O "$ASSETS/tokenizer.json" \
  "${BASE}/tokenizer.json?download=true" || echo "Tokenizer not found, using built-in fallback."

ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
echo "[✓] Kokoro model ready."
