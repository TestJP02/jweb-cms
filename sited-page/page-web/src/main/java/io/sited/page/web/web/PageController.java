package io.sited.page.web.web;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.sited.message.MessagePublisher;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.web.AbstractPageWebController;
import io.sited.page.web.service.CachedCategoryService;
import io.sited.page.web.service.CachedPageService;
import io.sited.web.NotFoundWebException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/{s:.+}")
public class PageController extends AbstractPageWebController {
    @Inject
    MessagePublisher<PageVisitedMessage> publisher;
    @Inject
    CachedPageService cachedPageService;
    @Inject
    CachedCategoryService cachedCategoryService;
    @Inject
    PageDraftWebService pageDraftWebService;
    @Inject
    UriInfo uriInfo;

    @GET
    public Response handle(@QueryParam("draft") String draft) {
        String path = normalize("/" + uriInfo.getPath());
        if (isCategory(path)) {
            return handleCategory(path);
        } else if (!Strings.isNullOrEmpty(draft)) {
            return handleDraft(path);
        } else {
            return handlePage(path);
        }
    }

    private String normalize(String path) {
        StringBuilder b = new StringBuilder();
        boolean hyphen = false;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '-') {
                if (!hyphen) {
                    b.append(Character.toLowerCase(c));
                    hyphen = true;
                }
            } else {
                hyphen = false;
                b.append(Character.toLowerCase(c));
            }
        }
        return b.toString();
    }

    private Response handlePage(String path) {
        Optional<PageResponse> pageOptional = cachedPageService.find(path);
        if (!pageOptional.isPresent()) {
            String categoryPath = path + "/";
            Optional<CategoryResponse> categoryOptional = cachedCategoryService.find(categoryPath);
            if (categoryOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(categoryPath)).build();
            } else {
                throw new NotFoundWebException("missing page, path={}", path);
            }
        }
        PageResponse page = pageOptional.get();
        CategoryResponse category = cachedCategoryService.get(page.categoryId);
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        notifyPageVisited(page);
        return page(page, bindings);
    }

    private Response handleDraft(String path) {
        Optional<DraftResponse> draft = pageDraftWebService.findByPath(path);
        if (!draft.isPresent()) {
            throw new NotFoundWebException("missing draft, path={}", path);
        }
        Map<String, Object> bindings = Maps.newHashMap();
        PageResponse page = response(draft.get());
        CategoryResponse category = cachedCategoryService.get(page.categoryId);
        bindings.put("category", category);
        notifyPageVisited(page);
        return page(page, bindings);
    }

    private Response handleCategory(String path) {
        Optional<CategoryResponse> categoryOptional = cachedCategoryService.find(path);
        if (!categoryOptional.isPresent()) {
            String pagePath = path.substring(0, path.length() - 1);
            Optional<PageResponse> pageOptional = cachedPageService.find(pagePath);
            if (pageOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(pagePath)).build();
            } else {
                throw new NotFoundWebException("missing page, path={}", path);
            }
        }
        Map<String, Object> bindings = Maps.newHashMap();
        CategoryResponse category = categoryOptional.get();
        bindings.put("category", category);
        return page(page(category), bindings);
    }

    private PageResponse response(DraftResponse page) {
        PageResponse response = new PageResponse();
        response.id = page.id;
        response.path = page.path;
        response.templatePath = page.templatePath;
        response.userId = page.userId;
        response.categoryId = page.categoryId;
        response.tags = page.tags;
        response.keywords = page.keywords;
        response.fields = page.fields;
        response.title = page.title;
        response.description = page.description;
        response.imageURL = page.imageURL;
        response.version = -1;
        response.status = page.status;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }

    private void notifyPageVisited(PageResponse page) {
        PageVisitedMessage message = new PageVisitedMessage();
        message.pageId = page.id;
        message.categoryId = page.categoryId;
        message.timestamp = OffsetDateTime.now();
        message.clientId = clientInfo.id();
        message.userId = userInfo.id();
        publisher.publish(message);
    }

    private boolean isCategory(String path) {
        return path.endsWith("/");
    }

    private PageResponse page(CategoryResponse category) {
        PageResponse page = new PageResponse();
        page.id = category.id;
        page.path = category.path;
        page.templatePath = category.templatePath;
        page.userId = category.ownerId;
        page.categoryId = category.parentId;
        page.title = category.displayName;
        page.keywords = category.keywords;
        page.description = category.description;
        page.tags = category.tags;
        page.fields = ImmutableMap.of();
        page.imageURLs = ImmutableList.of();
        return page;
    }
}

