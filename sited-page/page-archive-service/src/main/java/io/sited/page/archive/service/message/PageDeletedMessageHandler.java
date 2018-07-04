package io.sited.page.archive.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.archive.service.PageArchiveService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageDeletedMessageHandler implements MessageHandler<PageDeletedMessage> {
    @Inject
    PageArchiveService pageArchiveService;

    @Override
    public void handle(PageDeletedMessage message) throws Throwable {
        pageArchiveService.decrease(message.createdTime, message.updatedBy);
    }
}
