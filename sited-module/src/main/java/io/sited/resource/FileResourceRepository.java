package io.sited.resource;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author chi
 */
public class FileResourceRepository implements ResourceRepository {
    final Path dir;

    public FileResourceRepository(Path dir) {
        this.dir = dir;
    }

    @Override
    public Optional<Resource> get(String path) {
        File file = dir.resolve(path).toFile();
        if (file.exists() && file.isFile()) {
            return Optional.of(new FileResource(path, file.toPath()));
        }
        return Optional.empty();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void create(Resource resource) {
        try {
            Path path = dir.resolve(resource.path());
            Files.createDirectories(path.getParent());
            Files.write(path, resource.toByteArray());
            path.toFile().setLastModified(resource.lastModified());
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public void delete(String path) {
        dir.resolve(path).toFile().delete();
    }

    public void delete() {
        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(dir, fileVisitor);
        } catch (IOException e) {
            throw new ResourceException("failed to delete dir, path={}", dir, e);
        }
    }

    @Override
    public Iterator<Resource> iterator() {
        Deque<File> stack = new LinkedList<>();
        File[] children = dir.toFile().listFiles();
        if (children != null) {
            stack.addAll(Arrays.asList(children));
        }

        return new Iterator<Resource>() {
            File current;

            @Override
            public boolean hasNext() {
                while (!stack.isEmpty() && current == null) {
                    File file = stack.pollLast();
                    current = file;
                    File[] children = file.listFiles();
                    if (children != null && children.length != 0) {
                        for (File f : children) {
                            stack.addLast(f);
                        }
                    }
                }
                return current != null;
            }

            @Override
            public Resource next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                File next = current;
                current = null;
                return new FileResource(dir.relativize(next.toPath()).toString().replaceAll("\\\\", "/"), next.toPath());
            }
        };
    }

    public Path path() {
        return dir;
    }
}
