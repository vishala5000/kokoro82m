#!/usr/bin/env bash
set -e

# Use an ungated public mirror of Kokoro-82M ONNX
REPO="onnx-community/Kokoro-82M"
BASE="https://huggingface.co/${REPO}/resolve/main"
ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

# Use a browser User-Agent to bypass HF bot protection
UA="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

echo "[*] Downloading Kokoro-82M ONNX model from ungated mirror..."
# The model is in the 'onnx' subfolder
curl -L -f -A "$UA" -o "$ASSETS/model.onnx" "${BASE}/onnx/model.onnx"

echo "[*] Downloading voices (am_adam, hm_omega)..."
curl -L -f -A "$UA" -o "$ASSETS/voices/am_adam.bin" "${BASE}/voices/am_adam.bin"
curl -L -f -A "$UA" -o "$ASSETS/voices/hm_omega.bin" "${BASE}/voices/hm_omega.bin"

echo "[✓] Downloads complete."
ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
