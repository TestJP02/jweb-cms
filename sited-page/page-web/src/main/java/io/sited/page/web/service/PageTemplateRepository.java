package io.sited.page.web.service;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.template.TemplateResponse;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTemplateRepository implements ResourceRepository {
    @Inject
    ThemeService themeService;
    @Inject
    PageTemplateWebService pageTemplateWebService;

    @Override
    public Optional<Resource> get(String path) {
        Optional<TemplateResponse> pageTemplateResponseOptional = pageTemplateWebService.findByTemplatePath(path);
        if (pageTemplateResponseOptional.isPresent()) {
            TemplateResponse templateResponse = pageTemplateResponseOptional.get();
            return Optional.of(themeService.template(templateResponse));
        }
        return Optional.empty();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void create(Resource resource) {
    }

    @Override
    public void delete(String s) {
    }

    @Override
    public Iterator<Resource> iterator() {
        return ImmutableList.<Resource>of().iterator();
    }
}
