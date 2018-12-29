package app.jweb.page.web.web;

import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.PostInfo;
import app.jweb.page.web.service.CategoryCacheService;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.web.NotFoundWebException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
        return post(post(category), bindings);
    }

    private PostInfo post(CategoryResponse category) {
        return PostInfo.builder().setUserId(category.ownerId)
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
