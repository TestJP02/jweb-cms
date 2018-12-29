package app.jweb.page.web.service;

import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.component.SavedComponentResponse;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.resource.StringResource;
import com.google.common.collect.ImmutableList;

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
