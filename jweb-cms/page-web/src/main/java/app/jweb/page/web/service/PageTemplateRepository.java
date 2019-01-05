package app.jweb.page.web.service;

import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PageType;
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
    PageWebService pageWebService;

    @Override
    public Optional<Resource> get(String path) {
        Optional<PageResponse> pageTemplateResponseOptional = pageWebService.findByTemplatePath(path);
        if (pageTemplateResponseOptional.isPresent()) {
            PageResponse pageResponse = pageTemplateResponseOptional.get();
            if (pageResponse.type == PageType.CUSTOMIZED) {
                return Optional.of(pageTemplateResourceConverter.convert(pageResponse));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> list(String directory) {
        return ImmutableList.of();
    }
}
