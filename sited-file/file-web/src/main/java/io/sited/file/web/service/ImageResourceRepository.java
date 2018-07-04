package io.sited.file.web.service;

import io.sited.resource.FileResourceRepository;

import java.nio.file.Path;

/**
 * @author chi
 */
public class ImageResourceRepository extends FileResourceRepository {
    public ImageResourceRepository(Path dir) {
        super(dir);
    }
}
