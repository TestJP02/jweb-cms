package app.jweb.file.service;

import app.jweb.resource.CompositeResourceRepository;
import app.jweb.resource.ResourceRepository;

/**
 * @author chi
 */
public class ResourceManager extends CompositeResourceRepository {
    public ResourceManager(ResourceRepository... repositories) {
        super(repositories);
    }
}
