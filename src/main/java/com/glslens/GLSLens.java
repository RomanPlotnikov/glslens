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

package com.glslens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/// A class for parsing and processing shaders in `Java` using native libraries.
///
/// Supports multithreaded usage by creating separate instances in each thread.
public class GLSLens implements AutoCloseable {
    private static final AtomicInteger instancesCount = new AtomicInteger();

    static {
        new NativeLibrary("glslens");
    }

    /// Default constructor.
    ///
    /// Launches the `glslang` process if there were no active instances of this
    /// class.
    public GLSLens() {
        if (instancesCount.incrementAndGet() == 1) {
            initializeProcess();
        }
    }

    /// Deactivates the current instance.
    ///
    /// The `glslang` process will only be terminated when all instances of this
    /// class have been closed.
    @Override
    public void close() {
        if (instancesCount.decrementAndGet() == 0) {
            finalizeProcess();
        }
    }

    /// Retrieves a list of uniforms that will be available after the shader
    /// is compiled at runtime.
    ///
    /// @param source  GLSL source code.
    /// @param version GLSL version used in the source code.
    /// @param stage   shader stage for which the code was written.
    /// @return list containing information about available uniforms.
    public List<UniformInfo> getLiveUniformsInfo(
            final String source,
            final int version,
            final EShLanguage stage
    ) {
        Objects.requireNonNull(source);
        UniformInfo[] info = getLiveUniformsInfo(source, version, stage.getValue());
        List<UniformInfo> uniformsInfo = new ArrayList<>(info.length);
        Collections.addAll(uniformsInfo, info);
        return uniformsInfo;
    }

    private native UniformInfo[] getLiveUniformsInfo(final String source, final int version, final int stage);

    private native void initializeProcess();

    private native void finalizeProcess();
}
