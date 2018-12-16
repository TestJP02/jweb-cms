package app.jweb.post.api;


import app.jweb.post.api.content.PostContentResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/post/content")
public interface PostContentWebService {
    @Path("/postId/{postId}")
    @GET
    PostContentResponse getByPostId(@PathParam("postId") String postId);

    @Path("/batch-get")
    @PUT
    List<PostContentResponse> batchGetByPostIds(List<String> postIds);
}
