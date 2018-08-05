package io.sited.web.impl;

import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class ThemedResourceRepository implements ResourceRepository {
    private final String theme;
    private final ResourceRepository repository;

    public ThemedResourceRepository(String theme, ResourceRepository repository) {
        this.theme = theme;
        this.repository = repository;
    }

    @Override
    public Optional<Resource> get(String path) {
        String themedPath = themedPath(path);
        Optional<Resource> resource = repository.get(themedPath);
        return resource.map(r -> new ThemedResource(path, r));
    }

    @Override
    public List<Resource> list(String directory) {
        String themedPath = themedPath(directory);
        String prefix = prefix();
        List<Resource> resources = repository.list(themedPath);
        return resources.stream().map(resource -> new ThemedResource(resource.path().substring(prefix.length()), resource)).collect(Collectors.toList());
    }

    private String themedPath(String path) {
        return prefix() + path;
    }

    private String prefix() {
        return "theme/" + theme + '/';
    }
}
