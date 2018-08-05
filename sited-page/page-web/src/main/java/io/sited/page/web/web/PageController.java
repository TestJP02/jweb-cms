package io.sited.page.web.web;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.sited.message.MessagePublisher;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.web.AbstractPageController;
import io.sited.page.web.PageInfo;
import io.sited.page.web.service.CategoryCacheService;
import io.sited.page.web.service.PageService;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
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
public class PageController extends AbstractPageController {
    @Inject
    MessagePublisher<PageVisitedMessage> publisher;
    @Inject
    PageService pageService;
    @Inject
    CategoryCacheService categoryCacheService;
    @Inject
    PageDraftWebService pageDraftWebService;
    @Inject
    TemplateEngine templateEngine;
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
        Optional<PageResponse> pageOptional = pageService.find(path);
        if (!pageOptional.isPresent()) {
            String categoryPath = path + "/";
            Optional<CategoryResponse> categoryOptional = categoryCacheService.findByPath(categoryPath);
            if (categoryOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(categoryPath)).build();
            } else {
                return tryTemplate(path).orElseThrow(() -> new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing page, path={}", path));
            }
        }
        PageResponse page = pageOptional.get();
        CategoryResponse category;
        if (page.categoryId == null) {
            category = categoryCacheService.findByPath("/").orElse(null);
        } else {
            category = categoryCacheService.get(page.categoryId);
        }
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        notifyPageVisited(page);
        return page(page(page), bindings);
    }

    private Optional<Response> tryTemplate(String path) {
        String templatePath = fullPath(path);
        if (templatePath.startsWith("component/") || templatePath.startsWith("template/")) {
            return Optional.empty();
        }

        Optional<Template> template = templateEngine.template(templatePath);
        if (template.isPresent()) {
            PageInfo build = PageInfo.builder()
                .setTemplatePath(templatePath)
                .build();
            return Optional.of(page(build));
        }
        return Optional.empty();
    }

    private String fullPath(String path) {
        if (path.endsWith(".html")) {
            return path.substring(1);
        } else {
            return path.substring(1) + ".html";
        }
    }

    private Response handleDraft(String path) {
        Optional<DraftResponse> draftOptional = pageDraftWebService.findByPath(path);
        if (!draftOptional.isPresent()) {
            throw new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing draft, path={}", path);
        }
        DraftResponse draft = draftOptional.get();
        CategoryResponse category = categoryCacheService.get(draft.categoryId);
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        return page(page(draft), bindings);
    }

    private Response handleCategory(String path) {
        Optional<CategoryResponse> categoryOptional = categoryCacheService.findByPath(path);
        if (!categoryOptional.isPresent()) {
            String pagePath = path.substring(0, path.length() - 1);
            Optional<PageResponse> pageOptional = pageService.find(pagePath);
            if (pageOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(pagePath)).build();
            } else {
                throw new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing page, path={}", path);
            }
        }
        CategoryResponse category = categoryOptional.get();
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        return page(page(category), bindings);
    }

    private PageInfo page(DraftResponse page) {
        return PageInfo.builder()
            .setId(page.id)
            .setPath(page.path)
            .setTemplatePath(page.templatePath)
            .setUserId(page.userId)
            .setCategoryId(page.categoryId)
            .setTags(page.tags)
            .setKeywords(page.keywords)
            .setFields(page.fields)
            .setTitle(page.title)
            .setDescription(page.description)
            .setImageURL(page.imageURL)
            .setContent(page.content)
            .setVersion(page.version)
            .setStatus(page.status)
            .setCreatedBy(page.createdBy)
            .setCreatedTime(page.createdTime)
            .setUpdatedBy(page.updatedBy)
            .setUpdatedTime(page.updatedTime)
            .build();
    }

    private PageInfo page(PageResponse page) {
        return PageInfo.builder()
            .setId(page.id)
            .setPath(page.path)
            .setTemplatePath(page.templatePath)
            .setUserId(page.userId)
            .setCategoryId(page.categoryId)
            .setTags(page.tags)
            .setKeywords(page.keywords)
            .setFields(page.fields)
            .setTitle(page.title)
            .setDescription(page.description)
            .setImageURL(page.imageURL)
            .setContent(page.content)
            .setVersion(page.version)
            .setStatus(page.status)
            .setCreatedBy(page.createdBy)
            .setCreatedTime(page.createdTime)
            .setUpdatedBy(page.updatedBy)
            .setUpdatedTime(page.updatedTime)
            .build();
    }

    private PageInfo page(CategoryResponse category) {
        return PageInfo.builder()
            .setId(category.id)
            .setPath(category.path)
            .setTemplatePath(category.templatePath)
            .setUserId(category.ownerId)
            .setCategoryId(category.id)
            .setTags(category.tags)
            .setKeywords(category.keywords)
            .setTitle(category.displayName)
            .setDescription(category.description)
            .setImageURL(category.imageURL)
            .setStatus(PageStatus.ACTIVE)
            .setCreatedBy(category.createdBy)
            .setCreatedTime(category.createdTime)
            .setUpdatedBy(category.updatedBy)
            .setUpdatedTime(category.updatedTime)
            .build();
    }

    private void notifyPageVisited(PageResponse page) {
        PageVisitedMessage message = new PageVisitedMessage();
        message.pageId = page.id;
        message.categoryId = page.categoryId;
        message.timestamp = OffsetDateTime.now();
        message.clientId = clientInfo.id();
        publisher.publish(message);
    }

    private boolean isCategory(String path) {
        return path.endsWith("/");
    }
}

