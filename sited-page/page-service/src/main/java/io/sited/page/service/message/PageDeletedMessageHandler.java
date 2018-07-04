package io.sited.page.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.service.PageStatisticsService;
import io.sited.page.service.PageTagService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageDeletedMessageHandler implements MessageHandler<PageDeletedMessage> {
    @Inject
    PageTagService tagService;
    @Inject
    PageStatisticsService pageStatisticsService;

    @Override
    public void handle(PageDeletedMessage message) {
        tagService.untag(message.tags, message.updatedBy);
        pageStatisticsService.delete(message.id);
    }
}
