package app.jweb.page.search.api;

import app.jweb.page.search.api.history.BatchDeleteSearchHistoryRequest;
import app.jweb.page.search.api.history.CreateSearchHistoryRequest;
import app.jweb.page.search.api.history.SearchHistoryQuery;
import app.jweb.page.search.api.history.SearchHistoryResponse;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/page/search/history")
public interface SearchHistoryWebService {
    @Path("/find")
    @PUT
    QueryResponse<SearchHistoryResponse> find(SearchHistoryQuery query);

    @POST
    SearchHistoryResponse create(CreateSearchHistoryRequest request);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteSearchHistoryRequest request);
}
