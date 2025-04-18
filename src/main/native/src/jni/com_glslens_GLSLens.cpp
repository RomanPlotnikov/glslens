// Copyright 2025 Roman Plotnikov. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#include "com_glslens_GLSLens.h"

#include <stdexcept>

#include "wrappers/shader_program.h"

extern "C" {
JNIEXPORT jobjectArray JNICALL Java_com_glslens_GLSLens_getLiveUniformsInfo(
    JNIEnv *env, jobject, jstring source, jint version, jint stage) {
  const char *pShaderSource = env->GetStringUTFChars(source, nullptr);

  try {
    ShaderProgram program(pShaderSource, version,
                          static_cast<EShLanguage>(stage));

    env->ReleaseStringUTFChars(source, pShaderSource);
    pShaderSource = nullptr;

    jclass javaClass = env->FindClass("glslang/TObjectReflection");
    jmethodID constructor =
        env->GetMethodID(javaClass, "<init>", "(Ljava/lang/String;IIIIIIIII)V");

    const jsize uniformsCount = program.getLiveUniformsCount();
    jobjectArray javaArray =
        env->NewObjectArray(uniformsCount, javaClass, nullptr);
    for (jsize index = 0; index < uniformsCount; index++) {
      const auto &uniform = program.getUniform(index);
      jstring name = env->NewStringUTF(uniform.name.c_str());
      jobject javaObject = env->NewObject(
          javaClass, constructor, name, uniform.offset, uniform.glDefineType,
          uniform.size, uniform.index, uniform.counterIndex, uniform.numMembers,
          uniform.arrayStride, uniform.topLevelArraySize,
          uniform.topLevelArrayStride);

      env->SetObjectArrayElement(javaArray, index, javaObject);
      env->DeleteLocalRef(name);
      env->DeleteLocalRef(javaObject);
    }

    return javaArray;
  } catch (const std::exception &error) {
    if (pShaderSource != nullptr) {
      env->ReleaseStringUTFChars(source, pShaderSource);
      pShaderSource = nullptr;
    }

    jclass exceptionClass = env->FindClass("java/lang/RuntimeException");
    env->ThrowNew(exceptionClass, error.what());
    return nullptr;
  }
}

JNIEXPORT void JNICALL Java_com_glslens_GLSLens_initializeProcess(JNIEnv *,
                                                                  jobject) {
  glslang::InitializeProcess();
}

JNIEXPORT void JNICALL Java_com_glslens_GLSLens_finalizeProcess(JNIEnv *,
                                                                jobject) {
  glslang::FinalizeProcess();
}
}
