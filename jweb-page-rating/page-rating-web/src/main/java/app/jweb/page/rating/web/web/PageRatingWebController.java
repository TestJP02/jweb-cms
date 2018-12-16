package app.jweb.page.rating.web.web;

import app.jweb.page.rating.api.RatingWebService;
import app.jweb.page.rating.api.rating.RatingRequest;
import app.jweb.page.rating.api.rating.RatingResponse;
import app.jweb.page.rating.web.web.rating.RatingAJAXRequest;
import app.jweb.page.rating.web.web.rating.RatingAJAXResponse;
import app.jweb.post.api.PostVoteWebService;
import app.jweb.post.api.vote.VotePostRequest;
import app.jweb.post.api.vote.VoteType;
import app.jweb.web.ClientInfo;
import app.jweb.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/web/api/rating/rate")
public class PageRatingWebController {
    @Inject
    RatingWebService ratingWebService;

    @Inject
    PostVoteWebService pageVoteWebService;

    @Inject
    UserInfo userInfo;

    @Inject
    ClientInfo clientInfo;

    @PUT
    public Response rate(RatingAJAXRequest request) {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.pageId = request.pageId;
        ratingRequest.userId = userInfo.id();
        ratingRequest.ip = clientInfo.ip();
        ratingRequest.score = request.score;
        ratingRequest.content = request.content;
        ratingRequest.requestBy = userInfo.isAuthenticated() ? ratingRequest.userId : ratingRequest.ip;
        RatingResponse ratingResponse = ratingWebService.rate(ratingRequest);

        if (request.score >= 3) {
            VotePostRequest votePageRequest = new VotePostRequest();
            votePageRequest.postId = request.pageId;
            votePageRequest.type = VoteType.LIKE;
            votePageRequest.ip = clientInfo.ip();
            votePageRequest.userId = userInfo.id();
            votePageRequest.count = 1;
            votePageRequest.requestBy = "WEB";

            pageVoteWebService.vote(votePageRequest);
        }

        RatingAJAXResponse ajaxResponse = new RatingAJAXResponse();
        ajaxResponse.averageScore = ratingResponse.averageScore;
        ajaxResponse.totalScored = ratingResponse.totalScored;
        return Response.ok(ajaxResponse).build();
    }
}
