package app.jweb.post.api;

import app.jweb.post.api.keyword.KeywordResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/post/keyword")
public interface PostKeywordWebService {
    @GET
    List<KeywordResponse> find();
}
