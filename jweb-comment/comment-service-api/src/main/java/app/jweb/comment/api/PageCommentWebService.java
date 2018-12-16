package app.jweb.comment.api;

import app.jweb.comment.api.comment.BatchDeleteCommentRequest;
import app.jweb.comment.api.comment.CommentNodeResponse;
import app.jweb.comment.api.comment.CommentQuery;
import app.jweb.comment.api.comment.CommentResponse;
import app.jweb.comment.api.comment.CommentTreeQuery;
import app.jweb.comment.api.comment.CreateCommentRequest;
import app.jweb.comment.api.comment.UpdateCommentRequest;
import app.jweb.comment.api.comment.VoteCommentRequest;
import app.jweb.util.collection.QueryResponse;

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
    CommentResponse toggleVote(@PathParam("id") String id, VoteCommentRequest request);

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
