#!/usr/bin/env bash
set -e

REPO="onnx-community/Kokoro-82M-v1.0-ONNX"
BASE="https://huggingface.co/${REPO}/resolve/main"
ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

echo "[*] Downloading Kokoro-82M ONNX fp16 model..."
wget -q --show-progress -O "$ASSETS/model.onnx" \
  "${BASE}/model.onnx?download=true"

echo "[*] Downloading voices (am_adam, hm_omega)..."
wget -q --show-progress -O "$ASSETS/voices/am_adam.bin" \
  "${BASE}/voices/am_adam.bin?download=true"
wget -q --show-progress -O "$ASSETS/voices/hm_omega.bin" \
  "${BASE}/voices/hm_omega.bin?download=true"

echo "[*] Downloading tokenizer..."
wget -q --show-progress -O "$ASSETS/tokenizer.json" \
  "${BASE}/tokenizer.json?download=true"

ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
echo "[✓] Kokoro model ready."
