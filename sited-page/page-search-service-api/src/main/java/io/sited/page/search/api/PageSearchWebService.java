package io.sited.page.search.api;

import io.sited.page.api.page.PageResponse;
import io.sited.page.search.api.page.SearchPageRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/page/search")
public interface PageSearchWebService {
    @PUT
    QueryResponse<PageResponse> search(SearchPageRequest request);

    @Path("/full-index")
    @GET
    void fullIndex();
}
