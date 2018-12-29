package app.jweb.page.web.service;

import app.jweb.cache.Cache;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.template.TemplateResponse;
import com.google.common.base.Strings;

import javax.inject.Inject;
import java.util.Optional;

/**
 * TODO: make template support JSON attributes, remove this class
 *
 * @author chi
 */
public class TemplateCacheService {
    private static final TemplateResponse NONE = new TemplateResponse();
    @Inject
    Cache<TemplateResponse> cache;

    @Inject
    PageTemplateWebService pageTemplateWebService;

    public TemplateResponse template(String path) {
        if (Strings.isNullOrEmpty(path)) {
            return NONE;
        }
        Optional<TemplateResponse> cached = cache.get(path);
        if (cached.isPresent()) {
            return cached.get();
        }
        Optional<TemplateResponse> response = pageTemplateWebService.findByTemplatePath(path);
        response.ifPresent(templateResponse -> cache.put(path, templateResponse));
        return response.orElse(NONE);
    }

    public void reload(String path) {
        cache.delete(path);
    }
}
