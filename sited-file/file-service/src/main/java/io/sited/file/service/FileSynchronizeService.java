package io.sited.file.service;

import com.google.common.collect.Maps;
import io.sited.ApplicationException;
import io.sited.file.api.directory.CreateDirectoryRequest;
import io.sited.file.api.file.CreateFileRequest;
import io.sited.file.domain.Directory;
import io.sited.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger logger = LoggerFactory.getLogger(FileSynchronizeService.class);
    private final Path path;
    @Inject
    FileService fileService;
    @Inject
    DirectoryService directoryService;

    public FileSynchronizeService(Path path) {
        this.path = path;
    }

    public void synchronize() {
        String prefix = path.toString().replaceAll("\\\\", "/");
        System.out.println(prefix);
        Map<String, Directory> map = Maps.newHashMap();
        SimpleFileVisitor fileVisitor = new SimpleFileVisitor<Path>() {
            public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attributes) throws IOException {
                String path = file.toString().replaceAll("\\\\", "/") + "/";
                path = path.replace(prefix, "");
                Optional<Directory> optional = directoryService.findByPath(path);
                if (!optional.isPresent()) {
                    CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
                    createDirectoryRequest.parentId = map.get(parent(path.substring(0, path.length() - 1))).id;
                    createDirectoryRequest.path = path;
                    createDirectoryRequest.requestBy = "synchronized";
                    Directory directory = directoryService.create(createDirectoryRequest);
                    logger.info("create directory, path={}", path);
                    map.put(path, directory);
                } else {
                    map.put(path, optional.get());
                }
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                String path = file.toString().replaceAll("\\\\", "/");
                path = path.replace(prefix, "");
                Optional<io.sited.file.domain.File> optional = fileService.findByPath(path);
                if (!optional.isPresent()) {
                    File realFile = file.toFile();
                    String directoryPath = parent(path);
                    CreateFileRequest createFileRequest = new CreateFileRequest();
                    createFileRequest.directoryId = map.get(directoryPath).id;
                    createFileRequest.path = path;
                    createFileRequest.length = realFile.length();
                    createFileRequest.fileName = realFile.getName();
                    createFileRequest.requestBy = "synchronized";
                    fileService.create(createFileRequest);
                    logger.info("create file, path={}, name={}", createFileRequest.path, createFileRequest.fileName);
                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(path, fileVisitor);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ResourceException("failed to synchronize files");
        }
    }

    private String parent(String path) {
        int i = path.lastIndexOf('/');
        if (i < 0) {
            throw new ApplicationException("invalid directory path, {}", path);
        }
        return path.substring(0, i + 1);
    }
}
