package app.jweb.post.api;


import app.jweb.post.api.draft.BatchDeleteDraftRequest;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftQuery;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.draft.UpdateDraftRequest;
import app.jweb.post.api.post.PostResponse;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/post/draft")
public interface PostDraftWebService {
    @Path("/{id}")
    @GET
    DraftResponse get(@PathParam("id") String id);

    @Path("/find")
    @PUT
    QueryResponse<DraftResponse> find(DraftQuery query);

    @Path("/api/post/{postId}/draft")
    @GET
    Optional<DraftResponse> findByPostId(@PathParam("postId") String postId);

    @Path("/path/{path}")
    @GET
    Optional<DraftResponse> findByPath(@PathParam("path") String path);

    @POST
    DraftResponse create(CreateDraftRequest request);

    @Path("/{id}")
    @PUT
    DraftResponse update(@PathParam("id") String id, UpdateDraftRequest request);

    @Path("/{id}/publish")
    @POST
    PostResponse publish(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteDraftRequest request);
}
