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
  std::vector<UniformInfo> uniforms;

  try {
    ShaderProgram program(pShaderSource, version,
                          static_cast<EShLanguage>(stage));
    uniforms = program.parseUniforms();
  } catch (const std::runtime_error &error) {
    env->ReleaseStringUTFChars(source, pShaderSource);

    jclass exceptionClass = env->FindClass("java/lang/RuntimeException");
    env->ThrowNew(exceptionClass, error.what());
    return nullptr;
  }

  env->ReleaseStringUTFChars(source, pShaderSource);

  jclass uniformClass = env->FindClass("com/glslens/UniformInfo");
  jmethodID constructor =
      env->GetMethodID(uniformClass, "<init>", "(Ljava/lang/String;II)V");
  jobjectArray jarray =
      env->NewObjectArray(uniforms.size(), uniformClass, nullptr);

  for (size_t index = 0; index < uniforms.size(); index++) {
    const auto &uniform = uniforms[index];
    jstring name = env->NewStringUTF(uniform.name.c_str());
    jobject juniformInfo = env->NewObject(uniformClass, constructor, name,
                                          uniform.arraySize, uniform.typeID);
    env->SetObjectArrayElement(jarray, index, juniformInfo);

    env->DeleteLocalRef(name);
    env->DeleteLocalRef(juniformInfo);
  }

  return jarray;
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
