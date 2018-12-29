package app.jweb.file.service;

import app.jweb.file.domain.Directory;
import com.google.common.collect.Maps;
import app.jweb.ApplicationException;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.api.file.CreateFileRequest;
import app.jweb.resource.ResourceException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class FileSynchronizeService {
    private final Path dir;
    @Inject
    FileService fileService;
    @Inject
    DirectoryService directoryService;

    public FileSynchronizeService(Path dir) {
        this.dir = dir;
    }

    public void synchronize() {
        String prefix = dir.toString().replaceAll("\\\\", "/");
        try {
            Files.walkFileTree(dir, new PathSimpleFileVisitor(prefix, fileService, directoryService));
        } catch (IOException e) {
            throw new ResourceException("failed to synchronize files", e);
        }
    }

    static class PathSimpleFileVisitor extends SimpleFileVisitor<Path> {
        final String prefix;
        final Map<String, Directory> map = Maps.newHashMap();
        final FileService fileService;
        final DirectoryService directoryService;

        PathSimpleFileVisitor(String prefix, FileService fileService, DirectoryService directoryService) {
            this.prefix = prefix;
            this.fileService = fileService;
            this.directoryService = directoryService;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attributes) throws IOException {
            String filePath = file.toString().replaceAll("\\\\", "/") + "/";
            filePath = filePath.replace(prefix, "");
            Optional<Directory> optional = directoryService.findByPath(filePath);
            if (!optional.isPresent()) {
                CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
                createDirectoryRequest.parentId = map.get(parent(filePath.substring(0, filePath.length() - 1))).id;
                createDirectoryRequest.path = filePath;
                createDirectoryRequest.requestBy = "synchronized";
                Directory directory = directoryService.create(createDirectoryRequest);
                map.put(filePath, directory);
            } else {
                map.put(filePath, optional.get());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
            String filePath = file.toString().replaceAll("\\\\", "/");
            filePath = filePath.replace(prefix, "");
            Optional<app.jweb.file.domain.File> optional = fileService.findByPath(filePath);
            if (!optional.isPresent()) {
                File realFile = file.toFile();
                String directoryPath = parent(filePath);
                CreateFileRequest createFileRequest = new CreateFileRequest();
                createFileRequest.directoryId = map.get(directoryPath).id;
                createFileRequest.path = filePath;
                createFileRequest.length = realFile.length();
                createFileRequest.fileName = realFile.getName();
                createFileRequest.requestBy = "synchronized";
                fileService.create(createFileRequest);
            }
            return FileVisitResult.CONTINUE;
        }

        private String parent(String path) {
            int i = path.lastIndexOf('/');
            if (i < 0) {
                throw new ApplicationException("invalid directory path, {}", path);
            }
            return path.substring(0, i + 1);
        }
    }
}
