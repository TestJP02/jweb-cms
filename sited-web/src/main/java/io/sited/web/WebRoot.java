package io.sited.web;


import io.sited.resource.CompositeResourceRepository;
import io.sited.resource.ResourceRepository;

/**
 * @author chi
 */
public class WebRoot extends CompositeResourceRepository {
    public WebRoot(ResourceRepository... repositories) {
        super(repositories);
    }
}
