#include <jni.h>
#include <stdio.h>
#include "util_FUSELink.h"

JNIEXPORT void JNICALL
Java_util_FUSELink_registerOperations(JNIEnv *env, jobject obj)
{
    printf("Hello World!\n");
}