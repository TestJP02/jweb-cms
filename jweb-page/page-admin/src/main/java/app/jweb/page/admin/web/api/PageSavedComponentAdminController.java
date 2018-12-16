package app.jweb.page.admin.web.api;


import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.component.BatchDeleteSavedComponentAJAXRequest;
import app.jweb.page.api.component.CreateSavedComponentRequest;
import app.jweb.page.api.component.SavedComponentQuery;
import app.jweb.page.api.component.SavedComponentResponse;
import app.jweb.page.api.component.UpdateSavedComponentRequest;
import app.jweb.page.admin.web.api.component.CreatePageSavedComponentAJAXRequest;
import app.jweb.page.admin.web.api.component.PageSavedComponentAJAXQuery;
import app.jweb.page.admin.web.api.component.PageSavedComponentAJAXResponse;
import app.jweb.page.admin.web.api.component.UpdatePageSavedComponentAJAXRequest;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.UserInfo;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author chi
 */
@Path("/admin/api/page/saved-component")
public class PageSavedComponentAdminController {
    @Inject
    PageSavedComponentWebService pageSavedComponentWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public PageSavedComponentAJAXResponse get(@PathParam("id") String id) {
        return componentAJAXView(pageSavedComponentWebService.get(id));
    }

    @RolesAllowed("GET")
    @Path("/find")
    @PUT
    public QueryResponse<PageSavedComponentAJAXResponse> find(PageSavedComponentAJAXQuery ajaxQuery) {
        return pageSavedComponentWebService.find(query(ajaxQuery)).map(this::componentAJAXView);
    }

    @RolesAllowed("CREATE")
    @POST
    public PageSavedComponentAJAXResponse create(CreatePageSavedComponentAJAXRequest ajaxRequest) {
        return componentAJAXView(pageSavedComponentWebService.create(createRequest(ajaxRequest, userInfo.username())));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public PageSavedComponentAJAXResponse update(@PathParam("id") String id, UpdatePageSavedComponentAJAXRequest ajaxRequest) {
        return componentAJAXView(pageSavedComponentWebService.update(id, updateRequest(ajaxRequest, userInfo.username())));
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void delete(BatchDeleteSavedComponentAJAXRequest batchDeleteSavedComponentAJAXRequest) {
        BatchDeleteSavedComponentAJAXRequest batchDeletePageComponentRequest = new BatchDeleteSavedComponentAJAXRequest();
        batchDeletePageComponentRequest.ids = batchDeleteSavedComponentAJAXRequest.ids;
        batchDeletePageComponentRequest.requestBy = userInfo.username();
        pageSavedComponentWebService.delete(batchDeletePageComponentRequest);
    }

    private PageSavedComponentAJAXResponse componentAJAXView(SavedComponentResponse response) {
        PageSavedComponentAJAXResponse ajaxResponse = new PageSavedComponentAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.name = response.name;
        ajaxResponse.componentName = response.componentName;
        ajaxResponse.status = response.status;
        ajaxResponse.displayName = response.displayName;
        ajaxResponse.attributes = response.attributes;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.updatedBy = response.updatedBy;
        return ajaxResponse;
    }

    private SavedComponentQuery query(PageSavedComponentAJAXQuery ajaxQuery) {
        SavedComponentQuery query = new SavedComponentQuery();
        query.query = ajaxQuery.query;
        query.status = ajaxQuery.status;
        query.page = ajaxQuery.page;
        query.limit = ajaxQuery.limit;
        return query;
    }

    private CreateSavedComponentRequest createRequest(CreatePageSavedComponentAJAXRequest ajaxRequest, String requestBy) {
        CreateSavedComponentRequest request = new CreateSavedComponentRequest();
        request.name = ajaxRequest.name;
        request.componentName = ajaxRequest.componentName;
        request.displayName = ajaxRequest.displayName;
        request.attributes = ajaxRequest.attributes;
        request.requestBy = requestBy;
        return request;
    }

    private UpdateSavedComponentRequest updateRequest(UpdatePageSavedComponentAJAXRequest ajaxRequest, String requestBy) {
        UpdateSavedComponentRequest request = new UpdateSavedComponentRequest();
        request.name = ajaxRequest.name;
        request.displayName = ajaxRequest.displayName;
        request.attributes = ajaxRequest.attributes;
        request.requestBy = requestBy;
        return request;
    }
}
