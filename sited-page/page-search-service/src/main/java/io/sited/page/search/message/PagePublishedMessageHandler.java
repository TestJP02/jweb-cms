package io.sited.page.search.message;


import io.sited.message.MessageHandler;
import io.sited.page.api.page.PagePublishedMessage;
import io.sited.page.search.service.PageSearchService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PagePublishedMessageHandler implements MessageHandler<PagePublishedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PagePublishedMessage message) throws Throwable {
        pageSearchService.index(message.id);
    }
}
