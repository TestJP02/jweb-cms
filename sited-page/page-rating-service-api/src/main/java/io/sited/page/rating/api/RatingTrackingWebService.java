package io.sited.page.rating.api;


import io.sited.page.rating.api.rating.RatingTrackingQuery;
import io.sited.page.rating.api.rating.RatingTrackingResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/rating/tracking")
public interface RatingTrackingWebService {
    @Path("/find")
    @PUT
    QueryResponse<RatingTrackingResponse> find(RatingTrackingQuery query);
}
