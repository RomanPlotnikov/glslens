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

/// Represents shader pipeline stages supported by the rendering API.
public enum EShLanguage {
    EShLangVertex(0),
    EShLangTessControl(1),
    EShLangTessEvaluation(2),
    EShLangGeometry(3),
    EShLangFragment(4),
    EShLangCompute(5),
    EShLangRayGen(6),
    EShLangRayGenNV(6),
    EShLangIntersect(7),
    EShLangIntersectNV(7),
    EShLangAnyHit(8),
    EShLangAnyHitNV(8),
    EShLangClosestHit(9),
    EShLangClosestHitNV(9),
    EShLangMiss(10),
    EShLangMissNV(10),
    EShLangCallable(11),
    EShLangCallableNV(11),
    EShLangTask(12),
    EShLangTaskNV(12),
    EShLangMesh(13),
    EShLangMeshNV(13);

    private final int value;

    EShLanguage(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /// Converts a shader stage string identifier to its corresponding
    /// enum value.
    ///
    /// Supported shader stage identifiers:
    /// - `vert` – vertex shader
    /// - `tesc` – tessellation control shader
    /// - `tese` – tessellation evaluation shader
    /// - `geom` – geometry shader
    /// - `frag` – fragment shader
    /// - `comp` – compute shader
    /// - `rgen` – ray generation shader
    /// - `rint` – ray intersection shader
    /// - `rahit` – ray any-hit shader
    /// - `rchit` – ray closest-hit shader
    /// - `rmiss` – ray miss shader
    /// - `call` – callable shader
    /// - `task` – task shader
    /// - `mesh` – mesh shader
    ///
    /// @param shaderStage string identifier of the shader stage.
    /// @return corresponding enum element, or `null` if no match found.
    public static EShLanguage parse(final String shaderStage) {
        switch (shaderStage) {
            case "vert":
                return EShLangVertex;
            case "tesc":
                return EShLangTessControl;
            case "tese":
                return EShLangTessEvaluation;
            case "geom":
                return EShLangGeometry;
            case "frag":
                return EShLangFragment;
            case "comp":
                return EShLangCompute;
            case "rgen":
                return EShLangRayGen;
            case "rint":
                return EShLangIntersect;
            case "rahit":
                return EShLangAnyHit;
            case "rchit":
                return EShLangClosestHit;
            case "rmiss":
                return EShLangMiss;
            case "call":
                return EShLangCallable;
            case "task":
                return EShLangTask;
            case "mesh":
                return EShLangMesh;
            default:
                return null;
        }
    }
}
