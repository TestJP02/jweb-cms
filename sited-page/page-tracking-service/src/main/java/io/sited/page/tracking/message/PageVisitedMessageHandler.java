package io.sited.page.tracking.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.tracking.service.PageTrackingService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageVisitedMessageHandler implements MessageHandler<PageVisitedMessage> {
    @Inject
    PageTrackingService pageTrackingService;

    @Override
    public void handle(PageVisitedMessage pageVisitedMessage) throws Throwable {
        pageTrackingService.track(pageVisitedMessage);
    }
}
