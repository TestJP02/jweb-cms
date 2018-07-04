package io.sited.page.api;

import io.sited.page.api.statistics.PageStatisticsQuery;
import io.sited.page.api.statistics.PageStatisticsResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/statistics")
public interface PageStatisticsWebService {
    @Path("/find")
    @GET
    QueryResponse<PageStatisticsResponse> find(PageStatisticsQuery query);

    @Path("/page/{pageId}")
    @GET
    Optional<PageStatisticsResponse> findById(@PathParam("pageId") String pageId);
}
