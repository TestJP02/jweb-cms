package io.sited.resource;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class CachedResourceRepository implements ResourceRepository {
    private final ResourceRepository cachedResourceRepository;
    private final ResourceRepository repository;

    public CachedResourceRepository(ResourceRepository cachedResourceRepository, ResourceRepository repository) {
        this.cachedResourceRepository = cachedResourceRepository;
        this.repository = repository;
    }

    @Override
    public Optional<Resource> get(String path) {
        Optional<Resource> cached = cachedResourceRepository.get(path);
        Optional<Resource> original = repository.get(path);
        if (!original.isPresent()) {
            return Optional.empty();
        }
        if (cached.isPresent() && cached.get().lastModified() == original.get().lastModified()) {
            return cached;
        }
        cachedResourceRepository.create(original.get());
        return original;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void create(Resource resource) {
    }

    @Override
    public void delete(String path) {
    }

    @Override
    public Iterator<Resource> iterator() {
        return cachedResourceRepository.iterator();
    }
}
