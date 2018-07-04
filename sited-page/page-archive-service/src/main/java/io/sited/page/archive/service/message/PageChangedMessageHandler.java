package io.sited.page.archive.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.archive.service.PageArchiveService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    PageArchiveService pageArchiveService;

    @Override
    public void handle(PageChangedMessage message) throws Throwable {
        if (message.firstPublished && message.categoryId != null) {
            pageArchiveService.increase(message.createdTime, message.updatedBy);
        }
    }
}
