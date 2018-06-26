package io.sited.page.web.message;

import io.sited.message.MessageHandler;
import io.sited.page.api.category.CategoryChangedMessage;
import io.sited.page.web.service.CachedCategoryService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CategoryChangedMessageHandler implements MessageHandler<CategoryChangedMessage> {
    @Inject
    CachedCategoryService pageManager;

    @Override
    public void handle(CategoryChangedMessage categoryChangedMessage) throws Throwable {
        pageManager.reload(categoryChangedMessage.path);
    }
}
