cmake_minimum_required(VERSION 3.10)
project(glslens)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED)

option(BUILD_EXTERNAL "Build external dependencies in /External" OFF)
option(GLSLANG_TESTS "Enable glslang testing" OFF)
option(GLSLANG_ENABLE_INSTALL "Enable glslang installation" OFF)
option(ENABLE_SPIRV "Enables SPIRV output support" OFF)
option(ENABLE_SPVREMAPPER "Enables building of SPVRemapper" OFF)
option(ENABLE_GLSLANG_BINARIES "Builds glslang and spirv-remap" OFF)
option(ENABLE_HLSL "Enables HLSL input support" OFF)
option(ENABLE_PCH "Enables Precompiled header" OFF)
add_subdirectory(external/glslang)

set(HEADERS
        glslens/wrappers/shader_program.h
        glslens/wrappers/uniform_info.h
        glslens/main.h
)

set(SOURCES
        glslens/wrappers/shader_program.cpp
        glslens/wrappers/uniform_info.cpp
        glslens/main.cpp
)

add_executable(glslens ${SOURCES})

target_link_libraries(glslens PRIVATE
        glslang
)
