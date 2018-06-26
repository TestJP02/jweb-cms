package io.sited.page.admin.service;

import com.google.common.collect.Maps;
import io.sited.page.admin.TagProvider;

import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTagManager {
    private final Map<String, TagProvider> providers = Maps.newHashMap();

    public Optional<TagProvider> provider(String language) {
        return Optional.ofNullable(providers.get(language));
    }

    public void setProvider(String language, TagProvider suggester) {
        providers.put(language, suggester);
    }
}
