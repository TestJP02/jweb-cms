package app.jweb.page.web.service;

import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.template.TemplateResponse;
import app.jweb.page.api.template.TemplateType;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import com.google.common.collect.ImmutableList;

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
