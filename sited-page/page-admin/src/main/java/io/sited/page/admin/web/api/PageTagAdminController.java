package io.sited.page.admin.web.api;


import io.sited.page.api.PageTagWebService;
import io.sited.page.api.tag.BatchDeletePageTagRequest;
import io.sited.page.api.tag.CreatePageTagRequest;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.UpdatePageTagRequest;
import io.sited.web.LoginRequired;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/admin/api/page/tag")
public class PageTagAdminController {
    @Inject
    PageTagWebService pageTagWebService;

    @Path("/{id}")
    @GET
    @LoginRequired
    public Response get(@PathParam("id") String id) {
        return Response.ok(pageTagWebService.get(id)).build();
    }

    @Path("/find")
    @PUT
    @LoginRequired
    public Response find(PageTagQuery query) {
        return Response.ok(pageTagWebService.find(query)).build();
    }

    @Path("/{id}")
    @PUT
    @LoginRequired
    public Response update(@PathParam("id") String id, UpdatePageTagRequest request) {
        return Response.ok(pageTagWebService.update(id, request)).build();
    }

    @POST
    @LoginRequired
    public Response create(CreatePageTagRequest request) {
        return Response.ok(pageTagWebService.create(request)).build();
    }

    @Path("/batch-delete")
    @POST
    @LoginRequired
    public void batchDelete(BatchDeletePageTagRequest request) {
        pageTagWebService.batchDelete(request);
    }
}
