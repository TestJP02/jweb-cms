package app.jweb.post.api;


import app.jweb.post.api.tag.BatchCreatePostTagRequest;
import app.jweb.post.api.tag.BatchDeletePostTagRequest;
import app.jweb.post.api.tag.BatchGetTagRequest;
import app.jweb.post.api.tag.CreatePostTagRequest;
import app.jweb.post.api.tag.PostTagNodeResponse;
import app.jweb.post.api.tag.PostTagQuery;
import app.jweb.post.api.tag.PostTagResponse;
import app.jweb.post.api.tag.PostTagTreeQuery;
import app.jweb.post.api.tag.UpdatePostTagRequest;
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
@Path("/api/post/tag")
public interface PostTagWebService {
    @Path("/{id}")
    @GET
    PostTagResponse get(@PathParam("id") String id);

    @Path("/batch-get")
    @PUT
    List<PostTagResponse> batchGet(BatchGetTagRequest request);

    @Path("/name/{name}")
    @GET
    Optional<PostTagResponse> findByName(@PathParam("name") String path);

    @Path("/find")
    @PUT
    QueryResponse<PostTagResponse> find(PostTagQuery query);

    @Path("/tree")
    @PUT
    List<PostTagNodeResponse> tree(PostTagTreeQuery query);

    @Path("/{id}/children")
    @PUT
    List<PostTagResponse> children(@PathParam("id") String id);

    @POST
    PostTagResponse create(CreatePostTagRequest request);

    @Path("/batch-create")
    @POST
    List<PostTagResponse> batchCreate(BatchCreatePostTagRequest request);

    @Path("/{id}")
    @PUT
    PostTagResponse update(@PathParam("id") String id, UpdatePostTagRequest request);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeletePostTagRequest request);
}
