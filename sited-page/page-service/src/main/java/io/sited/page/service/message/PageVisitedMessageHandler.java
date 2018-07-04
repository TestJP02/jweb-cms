package io.sited.page.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.PageOptions;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.service.PageStatisticsService;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chi
 */
public class PageVisitedMessageHandler implements MessageHandler<PageVisitedMessage> {
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Inject
    PageStatisticsService pageService;
    @Inject
    PageOptions options;

    @Override
    public void handle(PageVisitedMessage message) throws Throwable {
        pageService.visit(message.pageId, count(), message.requestBy);
    }

    private int count() {
        if (options.visitRate == null || options.visitRate < 2) {
            return 1;
        }
        return Math.abs(random.nextInt()) % options.visitRate + 1;
    }
}
