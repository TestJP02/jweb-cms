package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.api.post.CommentDeletedMessage;
import app.jweb.post.service.PostStatisticsService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CommentDeletedMessageHandler implements MessageHandler<CommentDeletedMessage> {
    @Inject
    PostStatisticsService postService;

    @Override
    public void handle(CommentDeletedMessage message) {
        postService.commentDeleted(message.postId, 1, "SYS");
    }
}
