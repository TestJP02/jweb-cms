package io.sited.test.impl;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TempDirectory {
    private final Path path;

    public TempDirectory() {
        this(com.google.common.io.Files.createTempDir().toPath());
    }

    public TempDirectory(Path dir) {
        this.path = dir;
    }

    public Path root() {
        return path;
    }

    public void delete() throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return deleteAndContinue(file);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return deleteAndContinue(dir);
            }

            private FileVisitResult deleteAndContinue(Path path) throws IOException {
                Files.delete(path);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}