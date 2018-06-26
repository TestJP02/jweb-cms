package io.sited.page.message;

import com.google.common.collect.Sets;
import io.sited.message.MessageHandler;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.service.PageKeywordService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PageChangedMessageHandler implements MessageHandler<PageChangedMessage> {
    @Inject
    PageKeywordService pageKeywordService;

    @Override
    public void handle(PageChangedMessage message) throws Throwable {
        if (message.keywords != null)
            pageKeywordService.update(message.path, Sets.newHashSet(message.keywords), message.updatedBy);
    }
}
