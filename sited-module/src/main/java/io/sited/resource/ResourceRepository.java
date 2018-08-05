package io.sited.resource;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public interface ResourceRepository {
    Optional<Resource> get(String path);

    List<Resource> list(String directory);
}
