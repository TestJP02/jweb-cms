package io.sited.page.web.service;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.template.TemplateType;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTemplateRepository implements ResourceRepository {
    @Inject
    PageTemplateResourceConverter pageTemplateResourceConverter;
    @Inject
    PageTemplateWebService pageTemplateWebService;

    @Override
    public Optional<Resource> get(String path) {
        Optional<TemplateResponse> pageTemplateResponseOptional = pageTemplateWebService.findByTemplatePath(path);
        if (pageTemplateResponseOptional.isPresent()) {
            TemplateResponse templateResponse = pageTemplateResponseOptional.get();
            if (templateResponse.type == TemplateType.CUSTOMIZED) {
                return Optional.of(pageTemplateResourceConverter.convert(templateResponse));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> list(String directory) {
        return ImmutableList.of();
    }
}
