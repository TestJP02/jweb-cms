package io.sited.resource;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class SingleResourceRepository implements ResourceRepository {
    private final Resource resource;

    public SingleResourceRepository(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Optional<Resource> get(String path) {
        return Optional.of(resource);
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
        return ImmutableList.of(resource).iterator();
    }
}
