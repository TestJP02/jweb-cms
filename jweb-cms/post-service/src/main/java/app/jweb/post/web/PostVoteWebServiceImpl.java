package app.jweb.post.web;

import app.jweb.post.api.PostVoteWebService;
import app.jweb.post.api.vote.VotePostRequest;
import app.jweb.post.api.vote.VotePostResponse;
import app.jweb.post.domain.PostVoteTracking;
import app.jweb.post.service.PostVoteTrackingService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PostVoteWebServiceImpl implements PostVoteWebService {
    @Inject
    PostVoteTrackingService postVoteTrackingService;

    @Override
    public VotePostResponse vote(VotePostRequest request) {
        return response(postVoteTrackingService.create(request));
    }

    private VotePostResponse response(PostVoteTracking postVoteTracking) {
        VotePostResponse response = new VotePostResponse();
        response.id = postVoteTracking.id;
        response.postId = postVoteTracking.postId;
        response.userId = postVoteTracking.userId;
        response.ip = postVoteTracking.ip;
        response.count = postVoteTracking.count;
        response.type = postVoteTracking.type;
        response.createdTime = postVoteTracking.createdTime;
        response.createdBy = postVoteTracking.createdBy;
        return response;
    }
}
