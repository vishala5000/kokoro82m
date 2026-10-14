#!/usr/bin/env bash
set -e
ASSETS="app/src/main/assets/espeak-ng"
mkdir -p "$ASSETS"

# espeak-ng data archive (portable)
URL="https://github.com/espeak-ng/espeak-ng/releases/download/1.51/espeak-ng-data-1.51.zip"
wget -q --show-progress -O /tmp/espeak.zip "$URL"
unzip -q /tmp/espeak.zip -d /tmp/espeak
cp -r /tmp/espeak/espeak-ng-data/* "$ASSETS/" || \
  cp -r /tmp/espeak/* "$ASSETS/"

ls "$ASSETS"
echo "[✓] espeak-ng data ready."
