package io.sited.resource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author chi
 */
public class CompositeResourceRepository implements ResourceRepository {
    private final List<ResourceRepository> repositories;

    public CompositeResourceRepository(ResourceRepository... repositories) {
        this.repositories = Lists.newArrayList();
        for (ResourceRepository repository : repositories) {
            this.repositories.add(0, repository);
        }
    }

    public CompositeResourceRepository add(ResourceRepository repository) {
        repositories.add(0, repository);
        return this;
    }

    @Override
    public Optional<Resource> get(String path) {
        for (ResourceRepository repository : repositories) {
            Optional<Resource> source = repository.get(path);
            if (source.isPresent()) {
                return source;
            }
        }
        return Optional.empty();
    }

    public List<ResourceRepository> repositories() {
        return ImmutableList.copyOf(repositories);
    }

    @Override
    public List<Resource> list(String directory) {
        List<Resource> resources = Lists.newArrayList();
        Set<String> visited = Sets.newHashSet();
        for (ResourceRepository repository : repositories) {
            List<Resource> list = repository.list(directory);
            for (Resource resource : list) {
                if (!visited.contains(resource.path())) {
                    resources.add(resource);
                    visited.add(resource.path());
                }
            }
        }
        return resources;
    }
}
