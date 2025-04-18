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

package glslang;

/// Contains reflection metadata about a shader object (uniform, interface block, etc.).
/// Mirrors the `glslang::TObjectReflection` C++ structure for Java interoperability.
///
/// Used to inspect shader resource properties including type information, memory layout,
/// and binding details. Particularly useful for:
///
/// - Uniform buffer object (UBO) layout verification
/// - Shader resource binding management
/// - Atomic counter introspection
public class TObjectReflection {
    /// The declared name of the object in GLSL source code.
    ///
    /// For anonymous interface blocks, this will be an empty string.
    public String name;

    /// Byte offset within the parent block structure.
    ///
    /// For uniform block members, this follows std140/std430 layout rules.
    public int offset;

    /// OpenGL type enumeration constant (`GL_FLOAT`, `GL_SAMPLER_2D`, etc.).
    ///
    /// Corresponds to the fundamental data type in GLSL.
    public int glDefineType;

    /// Total size in bytes:
    /// - Base types: size of the fundamental type.
    /// - Arrays: size of single element × array length.
    /// - Structures: cumulative size including padding.
    public int size;

    /// Binding location index:
    /// - Uniforms: corresponds to 'layout(location=X)'.
    /// - Interface blocks: block index.
    public int index;

    /// Atomic counter index (for atomic_uint types).
    /// Returns -1 if not an atomic counter.
    public int counterIndex;

    /// Child element count:
    /// - Structures: number of members.
    /// - Arrays: number of elements (all dimensions for multidimensional arrays).
    /// - Others: 0.
    public int numMembers;

    /// Stride between elements in non-top-level arrays (bytes).
    ///
    /// Valid when the object is part of an array below the top level.
    public int arrayStride;

    /// Number of elements in the top-level array dimension.
    ///
    /// Returns 0 for non-array types or single-element arrays.
    public int topLevelArraySize;

    /// Stride between elements in the top-level array dimension (bytes).
    ///
    /// Only valid when topLevelArraySize > 1.
    public int topLevelArrayStride;

    /// Constructs a shader object reflection descriptor.
    ///
    /// @param name                GLSL declared name.
    /// @param offset              byte offset in parent block.
    /// @param glDefineType        OpenGL type enumeration.
    /// @param size                total memory size in bytes.
    /// @param index               binding location or block index.
    /// @param counterIndex        atomic counter index (-1 if N/A).
    /// @param numMembers          child element count.
    /// @param arrayStride         inner array stride (bytes).
    /// @param topLevelArraySize   outer array element count.
    /// @param topLevelArrayStride outer array stride (bytes).
    public TObjectReflection(
            final String name,
            final int offset,
            final int glDefineType,
            final int size,
            final int index,
            final int counterIndex,
            final int numMembers,
            final int arrayStride,
            final int topLevelArraySize,
            final int topLevelArrayStride
    ) {
        this.name = name;
        this.offset = offset;
        this.glDefineType = glDefineType;
        this.size = size;
        this.index = index;
        this.counterIndex = counterIndex;
        this.numMembers = numMembers;
        this.arrayStride = arrayStride;
        this.topLevelArraySize = topLevelArraySize;
        this.topLevelArrayStride = topLevelArrayStride;
    }
}
