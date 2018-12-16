package app.jweb.post.admin.web.api;


import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.tag.BatchDeletePostTagRequest;
import app.jweb.post.api.tag.CreatePostTagRequest;
import app.jweb.post.api.tag.PostTagQuery;
import app.jweb.post.api.tag.UpdatePostTagRequest;
import app.jweb.web.LoginRequired;

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
@Path("/admin/api/post/tag")
public class PostTagAdminController {
    @Inject
    PostTagWebService postTagWebService;

    @Path("/{id}")
    @GET
    @LoginRequired
    public Response get(@PathParam("id") String id) {
        return Response.ok(postTagWebService.get(id)).build();
    }

    @Path("/find")
    @PUT
    @LoginRequired
    public Response find(PostTagQuery query) {
        return Response.ok(postTagWebService.find(query)).build();
    }

    @Path("/{id}")
    @PUT
    @LoginRequired
    public Response update(@PathParam("id") String id, UpdatePostTagRequest request) {
        return Response.ok(postTagWebService.update(id, request)).build();
    }

    @POST
    @LoginRequired
    public Response create(CreatePostTagRequest request) {
        return Response.ok(postTagWebService.create(request)).build();
    }

    @Path("/batch-delete")
    @POST
    @LoginRequired
    public void batchDelete(BatchDeletePostTagRequest request) {
        postTagWebService.batchDelete(request);
    }
}
