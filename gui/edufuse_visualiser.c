#include <node_api.h>
#include <stdio.h>

napi_env *env;

napi_value Init(napi_env nenv, napi_value exports) {
    env = nenv;
    return exports;
}

NAPI_MODULE(NODE_GYP_MODULE_NAME, Init)