package io.sited.page.web.service;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;
import io.sited.resource.StringResource;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class HtmlComponentTemplateRepository implements ResourceRepository {
    @Inject
    PageSavedComponentWebService pageSavedComponentWebService;

    @Override
    public Optional<Resource> get(String path) {
        if (path.startsWith("component/")) {
            String componentName = path.substring("component/".length(), path.length() - ".html".length());
            Optional<SavedComponentResponse> savedComponent = pageSavedComponentWebService.findByName(componentName);
            if (savedComponent.isPresent()) {
                return Optional.of(new StringResource(path, (String) savedComponent.get().attributes.get("innerHTML")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> list(String directory) {
        return ImmutableList.of();
    }
}
