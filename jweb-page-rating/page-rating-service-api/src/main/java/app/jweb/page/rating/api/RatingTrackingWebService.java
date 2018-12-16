package app.jweb.page.rating.api;


import app.jweb.page.rating.api.rating.RatingTrackingResponse;
import app.jweb.page.rating.api.rating.RatingTrackingQuery;
import app.jweb.util.collection.QueryResponse;

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
