package io.sited.page.web.service;

import com.google.common.base.Strings;
import io.sited.cache.Cache;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.template.TemplateResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * TODO: make template support JSON attributes, remove this class
 *
 * @author chi
 */
public class CachedTemplateService {
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
}
