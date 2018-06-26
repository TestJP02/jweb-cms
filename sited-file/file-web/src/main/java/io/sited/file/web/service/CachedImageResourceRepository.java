package io.sited.file.web.service;

import io.sited.resource.FileResourceRepository;

import java.nio.file.Path;

/**
 * @author chi
 */
public class CachedImageResourceRepository extends FileResourceRepository {
    public CachedImageResourceRepository(Path dir) {
        super(dir);
    }
}
