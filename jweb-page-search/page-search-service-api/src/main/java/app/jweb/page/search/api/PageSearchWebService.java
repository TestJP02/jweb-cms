package app.jweb.page.search.api;

import app.jweb.page.search.api.page.SearchPageQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/page/search")
public interface PageSearchWebService {
    @PUT
    QueryResponse<PostResponse> search(SearchPageQuery query);

    @Path("/full-index")
    @GET
    void fullIndex();
}
