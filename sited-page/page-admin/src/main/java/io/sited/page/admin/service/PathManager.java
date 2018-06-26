package io.sited.page.admin.service;

import com.google.common.collect.Maps;
import io.sited.page.admin.PathProvider;

import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PathManager {
    private final Map<String, PathProvider> providers = Maps.newHashMap();

    public Optional<PathProvider> provider(String language) {
        return Optional.ofNullable(providers.get(language));
    }

    public void setProvider(String language, PathProvider suggester) {
        providers.put(language, suggester);
    }
}
