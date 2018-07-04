package io.sited.page.search.service.message;


import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.search.service.PageSearchService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    PageSearchService pageSearchService;

    @Override
    public void handle(PageChangedMessage message) throws Throwable {
        if (message.categoryId != null) {
            pageSearchService.index(message.id);
        }
    }
}
