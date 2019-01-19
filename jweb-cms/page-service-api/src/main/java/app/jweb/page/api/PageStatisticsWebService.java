package app.jweb.page.api;

import app.jweb.page.api.statistics.BatchGetPageStatisticsRequest;
import app.jweb.page.api.statistics.PageStatisticsQuery;
import app.jweb.page.api.statistics.PageStatisticsResponse;
import app.jweb.page.api.statistics.PageStatusStatisticsView;
import app.jweb.page.api.statistics.UpdatePageStatisticsRequest;
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
@Path("/api/page/statistics")
public interface PageStatisticsWebService {
    @Path("/find")
    @PUT
    QueryResponse<PageStatisticsResponse> find(PageStatisticsQuery query);

    @Path("/batch-get")
    @PUT
    List<PageStatisticsResponse> batchGet(BatchGetPageStatisticsRequest request);

    @Path("/page/{pageId}")
    @GET
    Optional<PageStatisticsResponse> findById(@PathParam("pageId") String pageId);

    @Path("/page/{pageId}")
    @PUT
    PageStatisticsResponse update(@PathParam("pageId") String pageId, UpdatePageStatisticsRequest request);

    @Path("/status")
    @GET
    List<PageStatusStatisticsView> statusStatistics();
}
