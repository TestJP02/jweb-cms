package io.sited.page.web.service;

import io.sited.cache.Cache;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class CachedCategoryService {
    @Inject
    Cache<CategoryResponse> cache;
    @Inject
    PageCategoryWebService pageCategoryWebService;

    public Optional<CategoryResponse> find(String path) {
        String cacheKey = path;
        Optional<CategoryResponse> category = cache.get(cacheKey);
        if (!category.isPresent()) {
            category = pageCategoryWebService.findByPath(path);
            category.ifPresent(categoryVariableResponse -> cache.put(cacheKey, categoryVariableResponse));
        }
        return category;
    }

    public CategoryResponse get(String id) {
        Optional<CategoryResponse> categoryOptional = cache.get(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        CategoryResponse category = pageCategoryWebService.get(id);
        cache.put(id, category);
        return category;
    }

    public void reload(String path) {
        cache.delete(path);
    }
}
