package app.jweb.post.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.post.PostOptions;
import app.jweb.post.api.post.PostVisitedMessage;
import app.jweb.post.service.PostStatisticsService;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chi
 */
public class PostVisitedMessageHandler implements MessageHandler<PostVisitedMessage> {
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Inject
    PostStatisticsService postService;
    @Inject
    PostOptions options;

    @Override
    public void handle(PostVisitedMessage message) throws Throwable {
        postService.visit(message.postId, count(), message.requestBy);
    }

    private int count() {
        if (options.visitRate == null || options.visitRate < 2) {
            return 1;
        }
        return Math.abs(random.nextInt()) % options.visitRate + 1;
    }
}
