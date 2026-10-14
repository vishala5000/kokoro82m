#!/usr/bin/env bash
set -e

# Your exact GitHub release details
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

echo "[*] Installing lightweight PyTorch for voice conversion..."
pip install torch --index-url https://download.pytorch.org/whl/cpu -q

echo "[*] Converting am_adam voice to raw 256-float binary..."
curl -L -f -o "/tmp/am_adam.pt" "${BASE_URL}/am_adam.bin"
python3 -c "
import torch
data = torch.load('/tmp/am_adam.pt', map_location='cpu', weights_only=True)
if isinstance(data, dict): data = next(iter(data.values()))
data.float().flatten()[:256].cpu().numpy().tofile('$ASSETS/voices/am_adam.bin')
print('✓ am_adam converted successfully (exactly 256 floats)')
"

echo "[*] Converting hm_omega voice to raw 256-float binary..."
curl -L -f -o "/tmp/hm_omega.pt" "${BASE_URL}/hm_omega.bin"
python3 -c "
import torch
data = torch.load('/tmp/hm_omega.pt', map_location='cpu', weights_only=True)
if isinstance(data, dict): data = next(iter(data.values()))
data.float().flatten()[:256].cpu().numpy().tofile('$ASSETS/voices/hm_omega.bin')
print('✓ hm_omega converted successfully (exactly 256 floats)')
"

echo "[✓] All downloads and conversions complete!"
echo "Voice file sizes (should be exactly 1.0K each):"
ls -lh "$ASSETS/voices"
