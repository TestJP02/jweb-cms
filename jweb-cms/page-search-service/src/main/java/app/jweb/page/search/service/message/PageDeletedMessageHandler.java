package app.jweb.page.search.service.message;


import app.jweb.message.MessageHandler;
import app.jweb.page.search.service.PageSearchService;
import app.jweb.post.api.post.PostDeletedMessage;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageDeletedMessageHandler implements MessageHandler<PostDeletedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PostDeletedMessage message) throws Throwable {
        pageSearchService.removeIndex(message.id);
    }
}
