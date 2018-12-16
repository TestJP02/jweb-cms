package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.api.post.PostCreatedMessage;
import app.jweb.post.service.PostKeywordService;
import app.jweb.post.service.PostStatisticsService;
import app.jweb.post.service.PostTagService;
import com.google.common.collect.Sets;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PostCreatedMessageHandler implements MessageHandler<PostCreatedMessage> {
    @Inject
    PostKeywordService postKeywordService;

    @Inject
    PostTagService postTagService;

    @Inject
    PostStatisticsService postStatisticsService;

    @Override
    public void handle(PostCreatedMessage message) {
        postTagService.tag(message.tags, message.updatedBy);
        if (message.keywords != null)
            postKeywordService.update(message.path, Sets.newHashSet(message.keywords), message.updatedBy);
        postStatisticsService.createIfNoneExist(message.id, message.categoryId, message.updatedBy);
    }
}
