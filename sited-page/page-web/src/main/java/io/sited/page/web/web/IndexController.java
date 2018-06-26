package io.sited.page.web.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.AbstractPageWebController;
import io.sited.page.web.service.CachedCategoryService;
import io.sited.web.NotFoundWebException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/")
public class IndexController extends AbstractPageWebController {
    @Inject
    CachedCategoryService categoryService;

    @GET
    public Response get() {
        Optional<CategoryResponse> categoryOptional = categoryService.find("/");
        if (!categoryOptional.isPresent()) {
            throw new NotFoundWebException("missing category, path=/");
        }

        Map<String, Object> bindings = Maps.newHashMap();
        CategoryResponse category = categoryOptional.get();
        bindings.put("category", category);
        return page(page(category), bindings);
    }

    private PageResponse page(CategoryResponse category) {
        PageResponse page = new PageResponse();
        page.id = category.id;
        page.userId = category.ownerId;
        page.categoryId = category.parentId;
        page.templatePath = category.templatePath;
        page.title = category.displayName;
        page.keywords = category.keywords;
        page.description = category.description;
        page.tags = category.tags;
        page.fields = ImmutableMap.of();
        page.imageURLs = category.imageURL == null ? ImmutableList.of() : ImmutableList.of(category.imageURL);
        page.createdTime = category.createdTime;
        page.createdBy = category.createdBy;
        page.updatedTime = category.updatedTime;
        page.updatedBy = category.updatedBy;
        return page;
    }
}
