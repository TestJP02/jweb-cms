package io.sited.page.service.message;

import com.google.common.collect.Sets;
import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.service.PageKeywordService;
import io.sited.page.service.PageStatisticsService;
import io.sited.page.service.PageTagService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    PageKeywordService pageKeywordService;

    @Inject
    PageTagService pageTagService;

    @Inject
    PageStatisticsService pageStatisticsService;

    @Override
    public void handle(PageChangedMessage message) throws Throwable {
        pageTagService.tag(message.tags, message.updatedBy);
        if (message.keywords != null)
            pageKeywordService.update(message.path, Sets.newHashSet(message.keywords), message.updatedBy);

        pageStatisticsService.createIfNoneExist(message.id, message.updatedBy);
    }
}
