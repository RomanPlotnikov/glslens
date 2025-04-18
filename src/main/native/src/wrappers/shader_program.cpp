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

#include "shader_program.h"

#include <stdexcept>

ShaderProgram::ShaderProgram(const std::string &source, const int version,
                             const EShLanguage stage) {
  glslang::TShader shader(stage);
  const char *shaderStrings[1] = {source.c_str()};
  shader.setStrings(shaderStrings, 1);

  constexpr EShMessages messages = EShMsgDefault;
  const TBuiltInResource resource = createDefaultResource();
  if (!shader.parse(&resource, version, false, messages)) {
    throw std::runtime_error(
        "Failed to parse shader: " + std::string(shader.getInfoLog()) +
        "Shader: " + source);
  }

  program.addShader(&shader);

  if (!program.link(messages)) {
    throw std::runtime_error(
        "Failed to link shader: " + std::string(program.getInfoLog()) +
        "Shader: " + source);
  }

  if (!program.buildReflection()) {
    throw std::runtime_error("Failed to build reflection at shader: " +
                             std::string(program.getInfoLog()) +
                             "Shader: " + source);
  }
}

const glslang::TObjectReflection &
ShaderProgram::getUniform(const int index) const {
  return program.getUniform(index);
}

int ShaderProgram::getLiveUniformsCount() const {
  return program.getNumLiveUniformVariables();
}
