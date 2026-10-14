#!/usr/bin/env bash
set -e

GITHUB_USER="vishala5000"
GITHUB_REPO="kokoro82m"
RELEASE_TAG="kokoro"

BASE_URL="https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/download/${RELEASE_TAG}"
ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

echo "[*] Downloading Kokoro-82M ONNX model..."
if curl -L -f -o "$ASSETS/model.onnx" "${BASE_URL}/model.onnx" 2>/dev/null; then
    echo "✓ Downloaded model.onnx"
elif curl -L -f -o "$ASSETS/model.onnx" "${BASE_URL}/model_fp16.onnx" 2>/dev/null; then
    echo "✓ Downloaded model_fp16.onnx (renamed to model.onnx)"
else
    echo "❌ ERROR: Could not find model.onnx or model_fp16.onnx"
    exit 1
fi

echo "[*] Downloading voices..."
curl -L -f -o "$ASSETS/voices/am_adam.bin" "${BASE_URL}/am_adam.bin"
echo "✓ Downloaded am_adam.bin"

curl -L -f -o "$ASSETS/voices/hm_omega.bin" "${BASE_URL}/hm_omega.bin"
echo "✓ Downloaded hm_omega.bin"

echo "[*] Downloading tokenizer.json..."
curl -L -f -o "$ASSETS/tokenizer.json" "${BASE_URL}/tokenizer.json"
echo "✓ Downloaded tokenizer.json"

echo "[✓] All downloads complete!"
ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
