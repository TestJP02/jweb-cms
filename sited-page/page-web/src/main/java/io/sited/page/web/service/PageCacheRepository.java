package io.sited.page.web.service;

import io.sited.resource.FileResourceRepository;

import java.nio.file.Path;

/**
 * @author chi
 */
public class PageCacheRepository extends FileResourceRepository {
    public PageCacheRepository(Path dir) {
        super(dir);
    }
}
