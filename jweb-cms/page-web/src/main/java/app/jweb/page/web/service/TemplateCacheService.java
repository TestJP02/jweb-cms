package app.jweb.page.web.service;

import app.jweb.cache.Cache;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageResponse;
import com.google.common.base.Strings;

import javax.inject.Inject;
import java.util.Optional;

/**
 * TODO: make template support JSON attributes, remove this class
 *
 * @author chi
 */
public class TemplateCacheService {
    private static final PageResponse NONE = new PageResponse();
    @Inject
    Cache<PageResponse> cache;

    @Inject
    PageWebService pageWebService;

    public PageResponse template(String path) {
        if (Strings.isNullOrEmpty(path)) {
            return NONE;
        }
        Optional<PageResponse> cached = cache.get(path);
        if (cached.isPresent()) {
            return cached.get();
        }
        Optional<PageResponse> response = pageWebService.findByTemplatePath(path);
        response.ifPresent(templateResponse -> cache.put(path, templateResponse));
        return response.orElse(NONE);
    }

    public void reload(String path) {
        cache.delete(path);
    }
}
