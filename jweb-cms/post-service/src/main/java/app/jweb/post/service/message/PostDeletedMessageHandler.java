package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.api.post.PostDeletedMessage;
import app.jweb.post.service.PostStatisticsService;
import app.jweb.post.service.PostTagService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PostDeletedMessageHandler implements MessageHandler<PostDeletedMessage> {
    @Inject
    PostTagService tagService;
    @Inject
    PostStatisticsService postStatisticsService;

    @Override
    public void handle(PostDeletedMessage message) {
        tagService.untag(message.tags, message.updatedBy);
        postStatisticsService.delete(message.id);
    }
}
