package app.jweb.post.web;


import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostVoteWebService;
import app.jweb.post.api.vote.VotePostRequest;
import app.jweb.post.api.vote.VotePostResponse;
import app.jweb.post.api.vote.VoteType;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install(PostModuleImpl.class)
class PostVoteWebServiceImplTest {
    @Inject
    PostVoteWebService pageVoteWebService;

    @Test
    public void vote() {
        VotePostRequest request = new VotePostRequest();
        request.postId = UUID.randomUUID().toString();
        request.userId = UUID.randomUUID().toString();
        request.ip = "127.0.0.1";
        request.type = VoteType.LIKE;
        request.requestBy = "SYS";
        request.count = 1;

        VotePostResponse response = pageVoteWebService.vote(request);
        assertNotNull(response.id);
    }
}