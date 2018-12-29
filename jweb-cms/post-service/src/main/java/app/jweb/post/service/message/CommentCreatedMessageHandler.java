package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.api.post.CommentCreatedMessage;
import app.jweb.post.service.PostStatisticsService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CommentCreatedMessageHandler implements MessageHandler<CommentCreatedMessage> {
    @Inject
    PostStatisticsService postService;

    @Override
    public void handle(CommentCreatedMessage message) {
        postService.commentCreated(message.postId, 1, "SYS");
    }
}
