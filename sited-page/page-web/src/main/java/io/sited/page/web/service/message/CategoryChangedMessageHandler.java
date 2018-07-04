package io.sited.page.web.service.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.category.CategoryChangedMessage;
import io.sited.page.web.service.CategoryCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CategoryChangedMessageHandler implements MessageHandler<CategoryChangedMessage> {
    @Inject
    CategoryCacheService pageManager;

    @Override
    public void handle(CategoryChangedMessage categoryChangedMessage) throws Throwable {
        pageManager.reload(categoryChangedMessage.path);
    }
}
