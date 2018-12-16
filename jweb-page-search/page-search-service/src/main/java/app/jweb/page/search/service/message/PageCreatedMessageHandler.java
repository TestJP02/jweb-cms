package app.jweb.page.search.service.message;


import app.jweb.message.MessageHandler;
import app.jweb.page.search.service.PageSearchService;
import app.jweb.post.api.post.PostCreatedMessage;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageCreatedMessageHandler implements MessageHandler<PostCreatedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PostCreatedMessage message) throws Throwable {
        pageSearchService.index(message);
    }
}
