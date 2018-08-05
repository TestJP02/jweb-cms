package io.sited.page.web.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.web.AbstractPageController;
import io.sited.page.web.PageInfo;
import io.sited.page.web.service.CategoryCacheService;
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
public class IndexController extends AbstractPageController {
    @Inject
    CategoryCacheService categoryService;

    @GET
    public Response get() {
        Optional<CategoryResponse> categoryOptional = categoryService.findByPath("/");
        if (!categoryOptional.isPresent()) {
            throw new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing category, path=/");
        }

        Map<String, Object> bindings = Maps.newHashMap();
        CategoryResponse category = categoryOptional.get();
        bindings.put("category", category);
        return page(page(category), bindings);
    }

    private PageInfo page(CategoryResponse category) {
        return PageInfo.builder().setUserId(category.ownerId)
            .setCategoryId(category.id)
            .setTemplatePath(category.templatePath)
            .setTitle(category.displayName)
            .setDescription(category.description)
            .setKeywords(category.keywords)
            .setTags(category.tags)
            .setImageURL(category.imageURL)
            .setImageURLs(category.imageURL == null ? ImmutableList.of() : Lists.newArrayList(category.imageURL))
            .setCreatedTime(category.createdTime)
            .setCreatedBy(category.createdBy)
            .setUpdatedTime(category.updatedTime)
            .setUpdatedBy(category.updatedBy)
            .build();
    }
}
