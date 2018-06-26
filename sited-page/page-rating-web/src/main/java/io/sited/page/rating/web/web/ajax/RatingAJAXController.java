package io.sited.page.rating.web.web.ajax;

import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.api.rating.RatingRequest;
import io.sited.page.rating.api.rating.RatingResponse;
import io.sited.page.rating.web.web.ajax.rating.RatingAJAXRequest;
import io.sited.page.rating.web.web.ajax.rating.RatingAJAXResponse;
import io.sited.web.ClientInfo;
import io.sited.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author chi
 */
public class RatingAJAXController {
    @Inject
    RatingWebService ratingWebService;

    @Inject
    UserInfo userInfo;

    @Inject
    ClientInfo clientInfo;

    @Path("/web/api/rating/rate")
    @PUT
    public Response rate(RatingAJAXRequest ratingAJAXRequest, Request request) throws IOException {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.pageId = ratingAJAXRequest.pageId;
        ratingRequest.userId = userInfo.id();
        ratingRequest.ip = clientInfo.ip();
        ratingRequest.score = ratingAJAXRequest.score;
        ratingRequest.content = ratingAJAXRequest.content;
        ratingRequest.requestBy = userInfo.isAuthenticated() ? ratingRequest.userId : ratingRequest.ip;
        RatingResponse ratingResponse = ratingWebService.rate(ratingRequest);
        RatingAJAXResponse ajaxResponse = new RatingAJAXResponse();
        ajaxResponse.averageScore = ratingResponse.averageScore;
        ajaxResponse.totalScored = ratingResponse.totalScored;
        return Response.ok(ajaxResponse).build();
    }
}
