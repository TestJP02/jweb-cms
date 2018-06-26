package io.sited.resource;

import java.util.Optional;

/**
 * @author chi
 */
public interface ResourceRepository extends Iterable<Resource> {
    Optional<Resource> get(String path);

    boolean isReadOnly();

    void create(Resource resource);

    void delete(String path);
}
