package app.jweb.web;

import app.jweb.resource.Resource;

import java.nio.file.Path;
import java.util.Optional;

/**
 * @author chi
 */
public interface WebCache {
    Path path();

    Optional<Resource> get(String path);

    void create(Resource resource);

    void delete(String path);
}
