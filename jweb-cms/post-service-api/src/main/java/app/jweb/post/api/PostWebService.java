package app.jweb.post.api;


import app.jweb.post.api.post.BatchGetPostRequest;
import app.jweb.post.api.post.CreatePostRequest;
import app.jweb.post.api.post.DeletePostRequest;
import app.jweb.post.api.post.PostNavigationResponse;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PopularPostQuery;
import app.jweb.post.api.post.PopularPostResponse;
import app.jweb.post.api.post.RevertDeletePostRequest;
import app.jweb.post.api.post.TopFixedPostQuery;
import app.jweb.post.api.post.TrendingPostQuery;
import app.jweb.post.api.post.UpdatePostRequest;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/post")
public interface PostWebService {
    @Path("/{id}")
    @GET
    PostResponse get(@PathParam("id") String id);

    @Path("/batch-get")
    @PUT
    List<PostResponse> batchGet(BatchGetPostRequest request);

    @Path("/path/{path}")
    @GET
    Optional<PostResponse> findByPath(@PathParam("path") String path);

    @Path("/find")
    @PUT
    QueryResponse<PostResponse> find(PostQuery query);

    @Path("/related")
    @PUT
    List<PostResponse> findRelated(PostRelatedQuery query);

    @Path("/{id}/navigation")
    @PUT
    PostNavigationResponse navigation(@PathParam("id") String id);

    @POST
    PostResponse create(CreatePostRequest request);

    @Path("/{id}")
    @PUT
    PostResponse update(@PathParam("id") String id, UpdatePostRequest request);

    @Path("/batch-delete")
    @POST
    void batchDelete(DeletePostRequest request);

    @Path("/revert")
    @POST
    void revertDelete(RevertDeletePostRequest request);

    @Path("/popular")
    @PUT
    QueryResponse<PopularPostResponse> popular(PopularPostQuery query);

    @Path("/top-fixed")
    @PUT
    QueryResponse<PostResponse> topFixed(TopFixedPostQuery query);

    @Path("/trending")
    @PUT
    QueryResponse<PostResponse> trending(TrendingPostQuery query);
}

