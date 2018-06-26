package io.sited.page.tracking.api;

import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/page/tracking/most-visit")
public interface PageTrackingWebService {
    @PUT
    QueryResponse<PageTrackingResponse> find(PageTrackingQuery query);
}
