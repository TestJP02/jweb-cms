package io.sited.page.web.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.web.service.CachedPageContentService;
import io.sited.page.web.service.CachedPageService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    CachedPageService cachedPageService;

    @Inject
    CachedPageContentService cachedPageContentService;

    @Override
    public void handle(PageChangedMessage message) throws Throwable {
        cachedPageService.reload(message.path);
        cachedPageContentService.reload(message.id);
    }
}
