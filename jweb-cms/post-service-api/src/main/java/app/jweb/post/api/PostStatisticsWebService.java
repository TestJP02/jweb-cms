package app.jweb.post.api;

import app.jweb.post.api.statistics.BatchGetPostStatisticsRequest;
import app.jweb.post.api.statistics.PostStatisticsQuery;
import app.jweb.post.api.statistics.PostStatisticsResponse;
import app.jweb.post.api.statistics.UpdatePostStatisticsRequest;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/post/statistics")
public interface PostStatisticsWebService {
    @Path("/find")
    @PUT
    QueryResponse<PostStatisticsResponse> find(PostStatisticsQuery query);

    @Path("/batch-get")
    @PUT
    List<PostStatisticsResponse> batchGet(BatchGetPostStatisticsRequest request);

    @Path("/post/{postId}")
    @GET
    Optional<PostStatisticsResponse> findById(@PathParam("postId") String postId);

    @Path("/post/{postId}")
    @PUT
    PostStatisticsResponse update(@PathParam("postId") String postId, UpdatePostStatisticsRequest request);
}
