#include <jni.h>
#include <jni.h>
#include <jni.h>

//JNIEXPORT jstring  extern "C" jstring
//Java_com_xerox_studyrays_MainActivity_00024Keys_APIKey(JNIEnv *env, jobject thiz) {
//
//}


extern "C"
JNIEXPORT jstring

Java_com_xerox_studyrays_MainActivity_00024Keys_APIKey1(JNIEnv *env, jobject thiz) {
    return env ->NewStringUTF("Coding Meet");
}

extern "C"
JNIEXPORT jstring
JNICALL
        Java_com_xerox_studyrays_ui_screens_MainViewModel_00024Keys_kkk(JNIEnv * env, jobject
thiz) {
return env ->NewStringUTF("qJrXkDpAsLmNoGzVxCbIuYhEtFwSjXyH");
}

extern "C"
JNIEXPORT jstring
JNICALL
        Java_com_xerox_studyrays_ui_screens_MainViewModel_00024Keys_ivv(JNIEnv * env, jobject
thiz) {
return env ->NewStringUTF("9708913856094020");

}

