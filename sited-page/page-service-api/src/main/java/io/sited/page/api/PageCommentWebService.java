package io.sited.page.api;

import io.sited.page.api.comment.BatchDeleteCommentRequest;
import io.sited.page.api.comment.CommentNodeResponse;
import io.sited.page.api.comment.CommentQuery;
import io.sited.page.api.comment.CommentResponse;
import io.sited.page.api.comment.CommentTreeQuery;
import io.sited.page.api.comment.CreateCommentRequest;
import io.sited.page.api.comment.UpdateCommentRequest;
import io.sited.page.api.comment.VoteCommentRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * @author chi
 */
@Path("/api/page/comment")
public interface PageCommentWebService {
    @Path("/{id}")
    @GET
    CommentResponse get(@PathParam("id") String id);

    @PUT
    QueryResponse<CommentResponse> find(CommentQuery query);

    @Path("/tree")
    @PUT
    QueryResponse<CommentNodeResponse> tree(CommentTreeQuery query);

    @POST
    CommentResponse create(CreateCommentRequest request);

    @Path("/{id}/vote")
    @POST
    void toggleVote(@PathParam("id") String id, VoteCommentRequest request);

    @Path("/{id}")
    @PUT
    CommentResponse update(@PathParam("id") String id, UpdateCommentRequest request);

    @Path("/{id}")
    @DELETE
    void delete(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteCommentRequest request);
}
