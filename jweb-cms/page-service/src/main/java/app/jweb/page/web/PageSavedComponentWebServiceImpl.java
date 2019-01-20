package app.jweb.page.web;

import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.component.CreateSavedComponentRequest;
import app.jweb.page.api.component.DeleteSavedComponentRequest;
import app.jweb.page.api.component.SavedComponentQuery;
import app.jweb.page.api.component.SavedComponentResponse;
import app.jweb.page.api.component.UpdateSavedComponentRequest;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageSavedComponentWebServiceImpl implements PageSavedComponentWebService {
    @Inject
    PageSavedComponentService pageComponentService;

    @Override
    public QueryResponse<SavedComponentResponse> find() {
        return pageComponentService.find().map(this::response);
    }

    @Override
    public QueryResponse<SavedComponentResponse> find(SavedComponentQuery query) {
        return pageComponentService.find(query).map(this::response);
    }

    @Override
    public SavedComponentResponse get(String id) {
        return response(pageComponentService.get(id));
    }

    @Override
    public Optional<SavedComponentResponse> findById(String id) {
        return pageComponentService.findById(id).map(this::response);
    }

    @Override
    public Optional<SavedComponentResponse> findByName(String name) {
        return pageComponentService.findByName(name).map(this::response);
    }

    @Override
    public SavedComponentResponse create(CreateSavedComponentRequest request) {
        PageSavedComponent pageComponent = pageComponentService.create(request);
        return response(pageComponent);
    }

    @Override
    public SavedComponentResponse update(String id, UpdateSavedComponentRequest request) {
        PageSavedComponent pageComponent = pageComponentService.update(id, request);
        return response(pageComponent);
    }

    @Override
    public void delete(DeleteSavedComponentRequest request) {
        pageComponentService.delete(request);
    }

    private SavedComponentResponse response(PageSavedComponent pageComponent) {
        SavedComponentResponse response = new SavedComponentResponse();
        response.id = pageComponent.id;
        response.name = pageComponent.name;
        response.componentName = pageComponent.componentName;
        response.displayName = pageComponent.displayName;
        response.status = pageComponent.status;
        response.attributes = pageComponent.attributes == null ? null : JSON.fromJSON(pageComponent.attributes, Map.class);
        response.createdTime = pageComponent.createdTime;
        response.createdBy = pageComponent.createdBy;
        response.updatedTime = pageComponent.updatedTime;
        response.updatedBy = pageComponent.updatedBy;
        return response;
    }
}
