package io.sited.test.impl;


import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;

import java.io.IOException;
import java.nio.file.Path;

public class TempDirectory {
    private final Path path;

    public TempDirectory() {
        this(Files.createTempDir().toPath());
    }

    public TempDirectory(Path dir) {
        this.path = dir;
    }

    public Path root() {
        return path;
    }

    public void delete() throws IOException {
        MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE);
    }

}