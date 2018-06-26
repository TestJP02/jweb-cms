package io.sited.page.search.message;


import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.search.service.PageSearchService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageDeletedMessageHandler implements MessageHandler<PageDeletedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PageDeletedMessage message) throws Throwable {
        pageSearchService.removeIndex(message.id);
    }
}
