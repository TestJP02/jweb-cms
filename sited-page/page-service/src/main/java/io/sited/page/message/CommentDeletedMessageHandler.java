package io.sited.page.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.comment.CommentDeletedMessage;
import io.sited.page.service.PageService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CommentDeletedMessageHandler implements MessageHandler<CommentDeletedMessage> {
    @Inject
    PageService pageService;

    @Override
    public void handle(CommentDeletedMessage message) throws Throwable {
        pageService.commentDeleted(message.pageId, 1, "SYS");
    }
}
