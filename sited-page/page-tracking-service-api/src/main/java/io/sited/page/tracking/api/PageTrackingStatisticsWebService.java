package io.sited.page.tracking.api;

import io.sited.page.tracking.api.tracking.PageStatisticsQuery;
import io.sited.page.tracking.api.tracking.PageStatisticsResponse;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/page/tracking/statistics")
public interface PageTrackingStatisticsWebService {

    @PUT
    List<PageStatisticsResponse> find(PageStatisticsQuery query);
}
