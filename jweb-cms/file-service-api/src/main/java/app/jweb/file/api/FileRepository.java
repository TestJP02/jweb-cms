package app.jweb.file.api;

import app.jweb.resource.Resource;

import java.util.Optional;

/**
 * @author chi
 */
public interface FileRepository {
    Optional<Resource> get(String path);

    void create(Resource resource);

    void delete(String path);
}
