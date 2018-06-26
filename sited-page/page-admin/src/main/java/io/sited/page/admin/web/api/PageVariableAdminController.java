package io.sited.page.admin.web.api;

import com.google.common.collect.Lists;
import io.sited.page.admin.web.api.variable.CreateVariableAJAXRequest;
import io.sited.page.admin.web.api.variable.DeleteVariableAJAXRequest;
import io.sited.page.admin.web.api.variable.FindVariableAJAXRequest;
import io.sited.page.admin.web.api.variable.UpdateVariableAJAXRequest;
import io.sited.page.admin.web.api.variable.VariableAJAXResponse;
import io.sited.page.api.PageVariableWebService;
import io.sited.page.api.variable.CreateVariableRequest;
import io.sited.page.api.variable.DeleteVariableRequest;
import io.sited.page.api.variable.UpdateVariableRequest;
import io.sited.page.api.variable.VariableQuery;
import io.sited.page.api.variable.VariableResponse;
import io.sited.util.collection.QueryResponse;
import io.sited.web.UserInfo;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author chi
 */
@Path("/admin/api/page/variable")
public class PageVariableAdminController {
    @Inject
    PageVariableWebService pageVariableWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public VariableAJAXResponse get(@PathParam("id") String id) {
        return response(pageVariableWebService.get(id));
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<VariableAJAXResponse> find(FindVariableAJAXRequest request) {
        VariableQuery query = new VariableQuery();
        query.name = request.name;
        query.status = request.status;
        query.limit = request.limit;
        query.page = request.page;
        return pageVariableWebService.find(query).map(this::response);
    }

    @RolesAllowed("CREATE")
    @POST
    public VariableAJAXResponse create(CreateVariableAJAXRequest createVariableAJAXRequest) {
        CreateVariableRequest createVariableRequest = new CreateVariableRequest();
        createVariableRequest.name = createVariableAJAXRequest.name;
        createVariableRequest.fields = createVariableAJAXRequest.fields;
        createVariableRequest.requestBy = userInfo.username();
        return response(pageVariableWebService.create(createVariableRequest));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public VariableAJAXResponse update(@PathParam("id") String id, UpdateVariableAJAXRequest updateVariableAJAXRequest) {
        UpdateVariableRequest updateVariableRequest = new UpdateVariableRequest();
        updateVariableRequest.name = updateVariableAJAXRequest.name;
        updateVariableRequest.fields = updateVariableAJAXRequest.fields;
        updateVariableRequest.requestBy = userInfo.username();
        return response(pageVariableWebService.update(id, updateVariableRequest));
    }


    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @PUT
    public void delete(DeleteVariableAJAXRequest ajaxRequest) {
        DeleteVariableRequest deleteVariableRequest = new DeleteVariableRequest();
        deleteVariableRequest.ids = ajaxRequest.ids;
        deleteVariableRequest.requestBy = userInfo.username();
        pageVariableWebService.delete(deleteVariableRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        DeleteVariableRequest deleteVariableRequest = new DeleteVariableRequest();
        deleteVariableRequest.ids = Lists.newArrayList(id);
        deleteVariableRequest.requestBy = userInfo.username();
        pageVariableWebService.delete(deleteVariableRequest);
    }

    private VariableAJAXResponse response(VariableResponse request) {
        VariableAJAXResponse instance = new VariableAJAXResponse();
        instance.id = request.id;
        instance.name = request.name;
        instance.status = request.status;
        instance.fields = request.fields;
        instance.fieldNum = instance.fields.size();
        instance.createdBy = request.createdBy;
        instance.createdTime = request.createdTime;
        instance.updatedBy = request.updatedBy;
        instance.updatedTime = request.updatedTime;
        return instance;
    }
}
