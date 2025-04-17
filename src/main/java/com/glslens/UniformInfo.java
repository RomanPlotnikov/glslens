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

/// Metadata of a uniform variable from a shader.
public final class UniformInfo {
    /// Name of the uniform variable declared in the shader.
    ///
    /// Used as an identifier when passing values through the render API.
    public final String name;

    /// Number of elements in the array uniform.
    ///
    /// Contains 0 if the uniform is not an array.
    public final int arraySize;

    /// Type identifier of the uniform type.
    ///
    /// Used to specify the data type when passing values through the
    /// render API.
    public final int typeID;

    public UniformInfo(final String name, final int arraySize, final int typeID) {
        this.name = name;
        this.arraySize = arraySize;
        this.typeID = typeID;
    }
}
