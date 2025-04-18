cmake_minimum_required(VERSION 3.10)
project(glslens VERSION 1.0.0)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED)

find_package(JNI REQUIRED)

option(BUILD_EXTERNAL "Build external dependencies in /External" OFF)
option(GLSLANG_TESTS "Enable glslang testing" OFF)
option(GLSLANG_ENABLE_INSTALL "Enable glslang installation" OFF)
option(ENABLE_SPIRV "Enables SPIRV output support" OFF)
option(ENABLE_SPVREMAPPER "Enables building of SPVRemapper" OFF)
option(ENABLE_GLSLANG_BINARIES "Builds glslang and spirv-remap" OFF)
option(ENABLE_HLSL "Enables HLSL input support" OFF)
option(ENABLE_PCH "Enables Precompiled header" OFF)
add_subdirectory(third_party/glslang)

set(HEADERS
        src/jni/com_glslens_GLSLens.h
        src/wrappers/shader_program.h
)
set(SOURCES
        src/jni/com_glslens_GLSLens.cpp
        src/wrappers/shader_program.cpp
)

add_library(glslens SHARED ${SOURCES} ${HEADERS})

target_include_directories(glslens PRIVATE
        ${JNI_INCLUDE_DIRS}
        src/
)

target_link_libraries(glslens PRIVATE
        ${JNI_LIBRARIES}
        glslang
)
