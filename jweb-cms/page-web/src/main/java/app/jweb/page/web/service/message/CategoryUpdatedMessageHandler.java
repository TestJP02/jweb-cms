package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.category.CategoryUpdatedMessage;
import app.jweb.page.web.service.CategoryCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CategoryUpdatedMessageHandler implements MessageHandler<CategoryUpdatedMessage> {
    @Inject
    CategoryCacheService categoryService;

    @Override
    public void handle(CategoryUpdatedMessage message) {
        categoryService.reload(message.path);
    }
}
