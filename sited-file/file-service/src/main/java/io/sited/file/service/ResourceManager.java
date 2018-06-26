package io.sited.file.service;

import io.sited.resource.CompositeResourceRepository;
import io.sited.resource.ResourceRepository;

/**
 * @author chi
 */
public class ResourceManager extends CompositeResourceRepository {
    public ResourceManager(ResourceRepository... repositories) {
        super(repositories);
    }
}
