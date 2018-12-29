package app.jweb.page.rating.api;


import app.jweb.page.rating.api.rating.RatingRequest;
import app.jweb.page.rating.api.rating.BatchDeleteRatingRequest;
import app.jweb.page.rating.api.rating.RatingResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/rating")
public interface RatingWebService {
    @Path("/{id}")
    @GET
    RatingResponse get(@PathParam("id") String id);

    @Path("/page/{pageId}")
    @GET
    Optional<RatingResponse> findByPageId(@PathParam("pageId") String pageId);

    @Path("/batch-get")
    @PUT
    List<RatingResponse> batchGet(List<String> ids);

    @POST
    RatingResponse rate(RatingRequest request);

    @Path("/batch-delete")
    @PUT
    void batchDelete(BatchDeleteRatingRequest request);
}
