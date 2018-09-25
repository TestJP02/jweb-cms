package app.jweb.resource;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class FileResourceRepository implements ResourceRepository {
    private final Logger logger = LoggerFactory.getLogger(FileResourceRepository.class);

    final Path dir;

    public FileResourceRepository(Path dir) {
        this.dir = dir;
        if (!this.dir.toFile().exists() || !this.dir.toFile().isDirectory()) {
            logger.warn("invalid directory, path={}", dir);
        }
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
    public List<Resource> list(String directory) {
        if (!dir.toFile().exists()) {
            return ImmutableList.of();
        }
        List<Resource> resources = Lists.newArrayList();

        LinkedList<File> stack = Lists.newLinkedList();
        File[] children = dir.toFile().listFiles();
        if (children != null) {
            stack.addAll(Arrays.asList(children));
        }

        while (!stack.isEmpty()) {
            File current = stack.pollFirst();
            if (current == null) {
                break;
            }
            String path = dir.relativize(current.toPath()).toString().replaceAll("\\\\", "/");
            if (current.isDirectory() && directory.startsWith(path)) {
                File[] files = current.listFiles();
                if (files != null) {
                    stack.addAll(Arrays.asList(files));
                }
            } else if (path.startsWith(directory)) {
                resources.add(new FileResource(path, current.toPath()));
            }
        }
        return resources;
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

    public Path path() {
        return dir;
    }
}
