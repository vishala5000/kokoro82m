cmake_minimum_required(VERSION 3.22.1)
project("espeak_jni")

# We ship a prebuilt espeak-ng static lib; the JNI wrapper just exposes it.
add_library(espeak_jni SHARED espeak_jni.cpp)

# Link against Android logging
target_link_libraries(espeak_jni log)
