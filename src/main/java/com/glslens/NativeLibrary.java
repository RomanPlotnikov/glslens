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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

class NativeLibrary {
    NativeLibrary(final String name) {
        try {
            loadLibraryFromJar(getPlatformPath(name));
        } catch (IOException ioException) {
            throw new RuntimeException("Failed to load native library.", ioException);
        }
    }

    private void loadLibraryFromJar(final String jarPath) throws IOException {
        if ((jarPath == null) || (!jarPath.startsWith("/"))) {
            throw new IllegalArgumentException("Invalid jar path: " + jarPath);
        }

        File tempFile = extractAndCreateTempFile(jarPath, getFilename(jarPath));
        System.load(tempFile.getAbsolutePath());
    }

    private String getFilename(final String jarPath) {
        int lastSlashIndex = jarPath.lastIndexOf('/');
        if (lastSlashIndex == (jarPath.length() - 1)) {
            throw new IllegalArgumentException("Jar path must point to the file: " + jarPath);
        }
        return jarPath.substring(lastSlashIndex + 1);
    }

    private File extractAndCreateTempFile(final String jarPath, final String filename)
            throws IOException {
        int dotIndex = filename.lastIndexOf('.');
        String prefix = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
        String suffix = (dotIndex == -1) ? null : filename.substring(dotIndex);

        File tempFile = File.createTempFile(prefix + "-", suffix);
        tempFile.deleteOnExit();

        try (InputStream inputStream = getResourceStream(jarPath);
             OutputStream outputStream = Files.newOutputStream(tempFile.toPath())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    private InputStream getResourceStream(final String jarPath) throws FileNotFoundException {
        InputStream inputStream = NativeLibrary.class.getResourceAsStream(jarPath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + jarPath);
        }
        return inputStream;
    }

    private String getPlatformPath(final String name) {
        String os = System.getProperty("os.name").toLowerCase();
        String jarPath = "/native/";
        if (os.startsWith("windows")) {
            return jarPath + (name + ".dll");
        } else if (os.startsWith("linux")) {
            return jarPath + ("lib" + name + ".so");
        } else if (os.startsWith("mac") || os.startsWith("darwin")) {
            return jarPath + ("lib" + name + ".dylib");
        }
        throw new RuntimeException("Platform is not supported: " + os);
    }
}
