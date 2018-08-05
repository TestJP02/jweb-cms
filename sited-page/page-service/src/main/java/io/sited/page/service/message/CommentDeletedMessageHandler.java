package io.sited.page.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.CommentDeletedMessage;
import io.sited.page.service.PageStatisticsService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CommentDeletedMessageHandler implements MessageHandler<CommentDeletedMessage> {
    @Inject
    PageStatisticsService pageService;

    @Override
    public void handle(CommentDeletedMessage message) {
        pageService.commentDeleted(message.pageId, 1, "SYS");
    }
}
