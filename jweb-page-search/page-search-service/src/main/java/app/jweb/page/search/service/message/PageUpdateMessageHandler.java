package app.jweb.page.search.service.message;


import app.jweb.message.MessageHandler;
import app.jweb.page.search.service.PageSearchService;
import app.jweb.post.api.post.PostUpdatedMessage;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageUpdateMessageHandler implements MessageHandler<PostUpdatedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PostUpdatedMessage message) {
        pageSearchService.index(message);
    }
}
