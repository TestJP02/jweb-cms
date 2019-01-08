package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.api.category.CategoryDeletedMessage;
import app.jweb.page.web.service.CategoryCacheService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class CategoryDeletedMessageHandler implements MessageHandler<CategoryDeletedMessage> {
    @Inject
    CategoryCacheService categoryService;

    @Override
    public void handle(CategoryDeletedMessage message) {
        categoryService.reload(message.id);
    }
}
