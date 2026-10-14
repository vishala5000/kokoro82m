#!/usr/bin/env bash
set -e

# ⚠️ REPLACE THESE with your actual GitHub username, repo name, and release tag
GITHUB_USER="YOUR_GITHUB_USERNAME"
GITHUB_REPO="YOUR_REPO_NAME"
RELEASE_TAG="v1.0"

BASE_URL="https://github.com/${GITHUB_USER}/${GITHUB_REPO}/releases/download/${RELEASE_TAG}"
ASSETS="app/src/main/assets/kokoro"
mkdir -p "$ASSETS/voices"

echo "[*] Downloading Kokoro-82M ONNX model from GitHub Release..."
curl -L -f -o "$ASSETS/model.onnx" "${BASE_URL}/model.onnx"

echo "[*] Downloading voices (am_adam, hm_omega)..."
curl -L -f -o "$ASSETS/voices/am_adam.bin" "${BASE_URL}/am_adam.bin"
curl -L -f -o "$ASSETS/voices/hm_omega.bin" "${BASE_URL}/hm_omega.bin"

echo "[✓] Downloads complete."
ls -lh "$ASSETS"
ls -lh "$ASSETS/voices"
