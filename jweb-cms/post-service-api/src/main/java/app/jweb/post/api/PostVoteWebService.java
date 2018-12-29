package app.jweb.post.api;

import app.jweb.post.api.vote.VotePostRequest;
import app.jweb.post.api.vote.VotePostResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/post/vote")
public interface PostVoteWebService {
    @POST
    VotePostResponse vote(VotePostRequest request);
}
