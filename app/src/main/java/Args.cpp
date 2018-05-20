#include "Args.h"

int argc = -1;
char **argv = NULL;

static void getArgs(int _argc, char **_argv, char **_env) {
  argc = _argc;
  argv = _argv;
}

JNIEXPORT jobjectArray JNICALL Java_Args_getArgs(JNIEnv *env, jclass thisObj) {
  jobjectArray arr = env->NewObjectArray(argc, env->FindClass("java/lang/String"), NULL);
  for (int i = 0 ; i < argc ; i++) {
    env->SetObjectArrayElement(arr, i,  env->NewStringUTF(argv[i]));
  }

  return arr;
}

__attribute__((section(".init_array"))) static void *ctr = (void*) getArgs;
