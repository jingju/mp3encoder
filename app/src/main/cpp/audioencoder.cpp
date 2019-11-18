#include <jni.h>
#include <string>
#include "mp3_encoder.h"
#include "CommonTools.h"

#define LOG_TAG "Mp3Encoder"

Mp3Encoder *encoder = NULL;

extern "C"
JNIEXPORT jint JNICALL
Java_com_jingju_lamemp3encoder_Mp3Encoder_init(JNIEnv *env, jobject instance, jstring pcmPath_,
                                               jint audioChannels, jint bitRate, jint sampleRate,
                                               jstring mp3Path_) {
    const char *pcmPath = env->GetStringUTFChars(pcmPath_, NULL);
    const char *mp3Path = env->GetStringUTFChars(mp3Path_, NULL);

    // TODO
    LOGI("mp3Path is %s...", mp3Path);
    encoder = new Mp3Encoder();
    int ret=encoder->Init(pcmPath, mp3Path, sampleRate, audioChannels, bitRate);

    env->ReleaseStringUTFChars(pcmPath_, pcmPath);
    env->ReleaseStringUTFChars(mp3Path_, mp3Path);
    return ret;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_jingju_lamemp3encoder_Mp3Encoder_destroy(JNIEnv *env, jobject instance) {
    if(NULL != encoder) {
        encoder->Destory();
        delete encoder;
        encoder = NULL;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_jingju_lamemp3encoder_Mp3Encoder_encode(JNIEnv *env, jobject instance) {
    if(NULL != encoder) {
        encoder->Encode();
    }
}