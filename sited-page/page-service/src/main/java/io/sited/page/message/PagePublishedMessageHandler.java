package io.sited.page.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PagePublishedMessage;
import io.sited.page.service.PageArchiveService;
import io.sited.page.service.PageTagService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PagePublishedMessageHandler implements MessageHandler<PagePublishedMessage> {
    @Inject
    PageArchiveService pageArchiveService;

    @Inject
    PageTagService pageTagService;

    @Override
    public void handle(PagePublishedMessage message) throws Throwable {
        pageArchiveService.increase(message.createdTime, message.updatedBy);
        pageTagService.tag(message.tags, message.updatedBy);
    }
}
