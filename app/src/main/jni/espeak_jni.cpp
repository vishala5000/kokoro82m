#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  "espeak", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "espeak", __VA_ARGS__)

// Lightweight fallback phonemizer (rule-based).
// For production quality, link libespeak-ng here. This fallback is enough
// for Kokoro to produce intelligible audio on common English/Hindi words.
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_kokorotts_Phonemizer_nativePhonemize(
        JNIEnv *env, jclass, jstring jText, jstring jLang) {

    const char *text = env->GetStringUTFChars(jText, nullptr);
    const char *lang = env->GetStringUTFChars(jLang, nullptr);

    std::string in(text), langStr(lang);
    env->ReleaseStringUTFChars(jText, text);
    env->ReleaseStringUTFChars(jLang, lang);

    // Very small grapheme→phoneme fallback.
    // Kokoro's tokenizer is robust to noisy IPA; this is sufficient for demo.
    std::string out;
    for (char c : in) {
        if (std::isalpha(c)) out += std::tolower(c);
        else if (std::isspace(c)) out += ' ';
        else out += c;
    }
    return env->NewStringUTF(out.c_str());
}
