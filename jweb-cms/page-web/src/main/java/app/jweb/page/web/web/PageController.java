package app.jweb.page.web.web;

import app.jweb.message.MessagePublisher;
import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.PostInfo;
import app.jweb.page.web.service.CategoryCacheService;
import app.jweb.page.web.service.PostService;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.post.PostVisitedMessage;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.web.NotFoundWebException;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

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
    MessagePublisher<PostVisitedMessage> publisher;
    @Inject
    PostService postService;
    @Inject
    CategoryCacheService categoryCacheService;
    @Inject
    PostDraftWebService postDraftWebService;
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
        Optional<PostResponse> postOptional = postService.find(path);
        if (!postOptional.isPresent()) {
            String categoryPath = path + "/";
            Optional<CategoryResponse> categoryOptional = categoryCacheService.findByPath(categoryPath);
            if (categoryOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(categoryPath)).build();
            } else {
                return tryTemplate(path).orElseThrow(() -> new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing page, path={}", path));
            }
        }
        PostResponse post = postOptional.get();
        CategoryResponse category;
        if (post.categoryId == null) {
            category = categoryCacheService.findByPath("/").orElse(null);
        } else {
            category = categoryCacheService.get(post.categoryId);
        }
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        notifyPageVisited(post);
        return post(post(post), bindings);
    }

    private Optional<Response> tryTemplate(String path) {
        String templatePath = fullPath(path);
        if (templatePath.startsWith("component/") || templatePath.startsWith("template/")) {
            return Optional.empty();
        }

        Optional<Template> template = templateEngine.template(templatePath);
        if (template.isPresent()) {
            PostInfo build = PostInfo.builder()
                .setTemplatePath(templatePath)
                .build();
            return Optional.of(post(build));
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
        Optional<DraftResponse> draftOptional = postDraftWebService.findByPath(path);
        if (!draftOptional.isPresent()) {
            throw new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing draft, path={}", path);
        }
        DraftResponse draft = draftOptional.get();
        CategoryResponse category = categoryCacheService.get(draft.categoryId);
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        return post(post(draft), bindings);
    }

    private Response handleCategory(String path) {
        Optional<CategoryResponse> categoryOptional = categoryCacheService.findByPath(path);
        if (!categoryOptional.isPresent()) {
            String postPath = path.substring(0, path.length() - 1);
            Optional<PostResponse> postOptional = postService.find(postPath);
            if (postOptional.isPresent()) {
                return Response.temporaryRedirect(URI.create(postPath)).build();
            } else {
                throw new NotFoundWebException(appInfo, requestInfo, clientInfo, "missing page, path={}", path);
            }
        }
        CategoryResponse category = categoryOptional.get();
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("category", category);
        return post(post(category), bindings);
    }

    private PostInfo post(DraftResponse draft) {
        return PostInfo.builder()
            .setId(draft.id)
            .setPath(draft.path)
            .setTemplatePath(draft.templatePath)
            .setUserId(draft.userId)
            .setCategoryId(draft.categoryId)
            .setTags(draft.tags)
            .setKeywords(draft.keywords)
            .setFields(draft.fields)
            .setTitle(draft.title)
            .setDescription(draft.description)
            .setImageURL(draft.imageURL)
            .setContent(draft.content)
            .setVersion(draft.version)
            .setStatus(draft.status)
            .setCreatedBy(draft.createdBy)
            .setCreatedTime(draft.createdTime)
            .setUpdatedBy(draft.updatedBy)
            .setUpdatedTime(draft.updatedTime)
            .build();
    }

    private PostInfo post(PostResponse post) {
        return PostInfo.builder()
            .setId(post.id)
            .setPath(post.path)
            .setTemplatePath(post.templatePath)
            .setUserId(post.userId)
            .setCategoryId(post.categoryId)
            .setTags(post.tags)
            .setKeywords(post.keywords)
            .setFields(post.fields)
            .setTitle(post.title)
            .setDescription(post.description)
            .setImageURL(post.imageURL)
            .setContent(post.content)
            .setVersion(post.version)
            .setStatus(post.status)
            .setCreatedBy(post.createdBy)
            .setCreatedTime(post.createdTime)
            .setUpdatedBy(post.updatedBy)
            .setUpdatedTime(post.updatedTime)
            .build();
    }

    private PostInfo post(CategoryResponse category) {
        return PostInfo.builder()
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
            .setStatus(PostStatus.ACTIVE)
            .setCreatedBy(category.createdBy)
            .setCreatedTime(category.createdTime)
            .setUpdatedBy(category.updatedBy)
            .setUpdatedTime(category.updatedTime)
            .build();
    }

    private void notifyPageVisited(PostResponse post) {
        PostVisitedMessage message = new PostVisitedMessage();
        message.postId = post.id;
        message.categoryId = post.categoryId;
        message.timestamp = OffsetDateTime.now();
        message.clientId = clientInfo.id();
        publisher.publish(message);
    }

    private boolean isCategory(String path) {
        return path.endsWith("/");
    }
}

