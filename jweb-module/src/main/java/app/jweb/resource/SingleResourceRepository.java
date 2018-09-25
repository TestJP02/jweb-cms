package app.jweb.resource;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        if (resource.path().equals(path)) {
            return Optional.of(resource);
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> list(String directory) {
        if (Pattern.compile(directory).matcher(resource.path()).matches()) {
            return ImmutableList.of(resource);
        }
        return ImmutableList.of();
    }
}
