package app.jweb.page.api;

import app.jweb.page.api.keyword.KeywordResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/page/keyword")
public interface PageKeywordWebService {
    @GET
    List<KeywordResponse> find();
}
