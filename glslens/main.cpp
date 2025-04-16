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

#include <iostream>
#include <sstream>
#include <stdexcept>
#include <string>
#include <vector>

#include "wrappers/shader_program.h"

int parseVersion(const std::string &versionStr) {
  int version = 0;
  try {
    version = std::stoi(versionStr);
  } catch (const std::exception &exception) {
    throw std::runtime_error(
        "Invalid version format: " + std::string(exception.what()) + '\n');
  }

  if ((version < 100) || (version > 460)) {
    throw std::runtime_error("Unsupported GLSL version: " + versionStr + '\n');
  }
  return version;
}

EShLanguage parseStage(const std::string &stageStr) {
  EShLanguage stage;
  try {
    int stageValue = std::stoi(stageStr);
    if ((stageValue < 0) || (stageValue > EShLangCount)) {
      throw std::out_of_range("Stage argument is out of enum range: " +
                              stageStr);
    }
    stage = static_cast<EShLanguage>(stageValue);
  } catch (const std::exception &exception) {
    throw std::runtime_error(
        "Invalid stage format: " + std::string(exception.what()) + '\n');
  }
  return stage;
}

void readStreamArguments(std::string &version, std::string &stage,
                         std::string &source) {
  if (!std::getline(std::cin, version)) {
    throw std::runtime_error("Failed to read version argument\n");
  }

  if (!std::getline(std::cin, stage)) {
    throw std::runtime_error("Failed to read stage argument\n");
  }

  std::stringstream sourceStream;
  std::string line;
  while (std::getline(std::cin, line)) {
    sourceStream << line << '\n';
  }

  source = sourceStream.str();
  if (source.empty()) {
    throw std::runtime_error("Empty shader source\n");
  }
}

int main() {
  if (!glslang::InitializeProcess()) {
    std::cerr << "Failed to initialize glslang\n";
    return EXIT_FAILURE;
  }

  std::string versionStr, stageStr, sourceStr;

  try {
    readStreamArguments(versionStr, stageStr, sourceStr);

    EShLanguage stage = parseStage(stageStr);
    int version = parseVersion(versionStr);
    ShaderProgram program{sourceStr, version, stage};

    for (const UniformInfo &info : program.parseUniforms()) {
      std::cout << info.name << ' ' << info.arraySize << ' ' << info.typeID
                << '\n';
    }
    std::cout.flush();
  } catch (const std::exception &exception) {
    std::cerr << "Error: " << exception.what() << '\n';
    glslang::FinalizeProcess();
    return EXIT_FAILURE;
  }
  glslang::FinalizeProcess();

  return EXIT_SUCCESS;
}
