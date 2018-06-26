package io.sited.page.web.service;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;
import io.sited.resource.StringResource;

import javax.inject.Inject;
import java.util.Iterator;
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
