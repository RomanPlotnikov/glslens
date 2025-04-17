// Copyright 2025 Roman Plotnikov. All rights reserved
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

#pragma once

#include <glslang/Public/ShaderLang.h>
#include <string>

/// \brief Metadata of a uniform variable from a shader.
struct UniformInfo {
public:
  /// \brief Name of the uniform variable declared in the shader.
  ///
  /// Used as an identifier when passing values through the rendering API.
  std::string name;

  /// \brief Number of elements in the array uniform.
  ///
  /// Contains 0 if the uniform is not an array.
  int arraySize;

  /// \brief Type identifier of the uniform type.
  ///
  /// Used to specify the data type when passing values through the rendering
  /// API.
  int typeID;

  /// \brief Constructs an object from glslang reflection data.
  ///
  /// \param uniformReflection reflection object representing a single uniform.
  UniformInfo(const glslang::TObjectReflection &uniformReflection);
};
