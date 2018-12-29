package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.api.post.PostUpdatedMessage;
import app.jweb.post.service.PostKeywordService;
import app.jweb.post.service.PostTagService;
import com.google.common.collect.Sets;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PostUpdatedMessageHandler implements MessageHandler<PostUpdatedMessage> {
    @Inject
    PostKeywordService postKeywordService;

    @Inject
    PostTagService postTagService;

    @Override
    public void handle(PostUpdatedMessage message) {
        postTagService.tag(message.tags, message.updatedBy);
        if (message.keywords != null)
            postKeywordService.update(message.path, Sets.newHashSet(message.keywords), message.updatedBy);
    }
}
