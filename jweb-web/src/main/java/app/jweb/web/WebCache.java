package app.jweb.web;

import app.jweb.resource.Resource;

import java.util.Optional;

/**
 * @author chi
 */
public interface WebCache {
    Optional<Resource> get(String path);

    void create(Resource resource);

    void delete(String path);
}
