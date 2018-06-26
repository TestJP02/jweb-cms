package io.sited.resource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author chi
 */
public class CompositeResourceRepository implements ResourceRepository {
    private final List<ResourceRepository> repositories;

    public CompositeResourceRepository(ResourceRepository... repositories) {
        this.repositories = Lists.newArrayList(repositories);
    }

    public CompositeResourceRepository add(ResourceRepository repository) {
        repositories.add(repository);
        return this;
    }

    @Override
    public Optional<Resource> get(String path) {
        for (ResourceRepository sourceRepository : repositories) {
            Optional<Resource> source = sourceRepository.get(path);
            if (source.isPresent()) {
                return source;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isReadOnly() {
        return repositories.stream().allMatch(ResourceRepository::isReadOnly);
    }

    @Override
    public void create(Resource resource) {
        repositories.stream()
            .filter(((Predicate<ResourceRepository>) ResourceRepository::isReadOnly).negate())
            .forEach(repository -> repository.create(resource));
    }

    @Override
    public void delete(String path) {
        repositories.stream()
            .filter(((Predicate<ResourceRepository>) ResourceRepository::isReadOnly).negate())
            .forEach(repository -> repository.delete(path));
    }

    @Override
    public Iterator<Resource> iterator() {
        if (repositories.isEmpty()) {
            return ImmutableList.<Resource>of().iterator();
        }
        return new ResourceIterator(repositories);
    }

    static class ResourceIterator implements Iterator<Resource> {
        private final List<ResourceRepository> repositories;
        private int index;
        private final Set<String> visited = Sets.newHashSet();
        private Iterator<Resource> current;
        private Resource next;

        ResourceIterator(List<ResourceRepository> repositories) {
            this.repositories = repositories;
            int index = 0;
            current = repositories.get(index).iterator();
        }

        @Override
        public boolean hasNext() {
            if (hasNext(current)) {
                return true;
            }
            index++;
            while (index < repositories.size()) {
                current = repositories.get(index).iterator();
                if (hasNext(current)) {
                    return true;
                }
                index++;
            }
            return false;
        }

        boolean hasNext(Iterator<Resource> iterator) {
            while (iterator.hasNext()) {
                next = iterator.next();
                if (!visited.contains(next.path())) {
                    visited.add(next.path());
                    return true;
                }
            }
            return false;
        }

        @Override
        public Resource next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            return next;
        }
    }
}
