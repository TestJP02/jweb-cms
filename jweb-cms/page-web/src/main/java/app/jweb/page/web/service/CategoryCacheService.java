package app.jweb.page.web.service;

import app.jweb.cache.Cache;
import app.jweb.page.api.PageCategoryWebService;
import app.jweb.page.api.category.CategoryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class CategoryCacheService {
    @Inject
    Cache<CategoryResponse> cache;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public CategoryResponse get(String id) {
        Optional<CategoryResponse> categoryOptional = cache.get(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        CategoryResponse category = pageCategoryWebService.get(id);
        cache.put(id, category);
        return category;
    }

    public void reload(String id) {
        cache.delete(id);
    }
}
