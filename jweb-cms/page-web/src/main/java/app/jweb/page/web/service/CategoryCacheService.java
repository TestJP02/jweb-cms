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
    PageCategoryWebService postCategoryWebService;

    public Optional<CategoryResponse> findByPath(String path) {
        String cacheKey = path;
        Optional<CategoryResponse> category = cache.get(cacheKey);
        if (!category.isPresent()) {
            category = postCategoryWebService.findByPath(path);
            category.ifPresent(categoryVariableResponse -> cache.put(cacheKey, categoryVariableResponse));
        }
        return category;
    }

    public CategoryResponse get(String id) {
        Optional<CategoryResponse> categoryOptional = cache.get(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        CategoryResponse category = postCategoryWebService.get(id);
        cache.put(id, category);
        return category;
    }

    public void reload(String path) {
        cache.delete(path);
    }
}
