package io.sited.page.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.service.PageArchiveService;
import io.sited.page.service.PageTagService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageDeletedMessageHandler implements MessageHandler<PageDeletedMessage> {
    @Inject
    PageArchiveService pageArchiveService;

    @Inject
    PageTagService tagService;

    @Override
    public void handle(PageDeletedMessage message) {
        pageArchiveService.decrease(message.createdTime, message.updatedBy);
        tagService.untag(message.tags, message.updatedBy);
    }
}
