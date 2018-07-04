package io.sited.template;

import io.sited.resource.Resource;

/**
 * @author chi
 */
public interface ElementProcessor {
    default int priority() {
        return 100;
    }

    Element process(Element element, Resource resource);
}
