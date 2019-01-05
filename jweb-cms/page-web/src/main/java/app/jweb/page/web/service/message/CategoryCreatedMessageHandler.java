package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.category.CategoryCreatedMessage;
import app.jweb.page.web.service.CategoryCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CategoryCreatedMessageHandler implements MessageHandler<CategoryCreatedMessage> {
    @Inject
    CategoryCacheService categoryService;

    @Override
    public void handle(CategoryCreatedMessage message) {
        categoryService.reload(message.path);
    }
}
