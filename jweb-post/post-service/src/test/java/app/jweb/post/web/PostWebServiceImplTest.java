package app.jweb.post.web;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.draft.UpdateDraftRequest;
import app.jweb.post.api.post.BatchGetPostRequest;
import app.jweb.post.api.post.PostNavigationResponse;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.TopFixedPostQuery;
import app.jweb.post.api.post.UpdatePostRequest;
import app.jweb.scheduler.SchedulerModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PostModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class PostWebServiceImplTest {
    @Inject
    PostWebService pageWebService;
    @Inject
    PostDraftWebService pageDraftWebService;

    @BeforeEach
    public void setup() throws InterruptedException {
        DraftResponse page1 = pageDraftWebService.create(create("/1.html", "title 1", "description 1"));
        pageDraftWebService.publish(page1.id, "test");

        DraftResponse page2 = pageDraftWebService.create(create("/2.html", "title 2", "description 2"));
        pageDraftWebService.publish(page2.id, "test");
        pageDraftWebService.create(create("/3.html", "title 3", "description 3"));
    }

    @Test
    public void updatePublished() {
        PostResponse page = pageWebService.findByPath("/1.html").get();
        CreateDraftRequest request = new CreateDraftRequest();
        request.path = page.path;
        request.postId = page.id;
        request.title = "title 3";
        request.description = "description 3";
        request.keywords = page.keywords;
        request.tags = page.tags;
        request.imageURL = page.imageURL;
        request.userId = UUID.randomUUID().toString();
        request.requestBy = "test";
        request.categoryId = page.categoryId;

        DraftResponse draft = pageDraftWebService.create(request);
        pageDraftWebService.publish(draft.id, "test");

        PostResponse updatedPost = pageWebService.findByPath("/1.html").get();
        assertEquals(page.id, updatedPost.id);
        assertEquals("title 3", updatedPost.title);
    }

    @Test
    public void topFixed() {
        CreateDraftRequest createDraftRequest = create("/top-fixed.html", "top fixed test", "test description");
        createDraftRequest.topFixed = true;
        DraftResponse draft = pageDraftWebService.create(createDraftRequest);
        PostResponse page = pageDraftWebService.publish(draft.id, "SYS");
        assertTrue(page.topFixed);

        TopFixedPostQuery query = new TopFixedPostQuery();
        QueryResponse<PostResponse> pages = pageWebService.topFixed(query);
        assertEquals(1, pages.items.size());
    }

    @Test
    public void update() {
        DraftResponse draft = pageDraftWebService.findByPath("/3.html").get();
        UpdateDraftRequest request = new UpdateDraftRequest();
        request.title = "title 3";
        request.description = "description 3";
        request.requestBy = "test";
        request.path = draft.path;
        request.categoryId = draft.categoryId;
        request.keywords = draft.keywords;
        request.tags = draft.tags;
        request.imageURL = draft.imageURL;

        DraftResponse updatedDraft = pageDraftWebService.update(draft.id, request);
        pageDraftWebService.publish(updatedDraft.id, "test");

        PostResponse updatedPost = pageWebService.findByPath("/3.html").get();
        assertEquals("title 3", updatedPost.title);
    }

    @Test
    public void findRelated() {
        PostResponse page = pageWebService.findByPath("/1.html").get();
        PostRelatedQuery query = new PostRelatedQuery();
        query.id = page.id;
        query.limit = 5;

        List<PostResponse> related = pageWebService.findRelated(query);
        assertEquals(1, related.size());
        assertNotEquals(page.id, related.get(0).id);
    }

    @Test
    public void navigation() {
        PostResponse page = pageWebService.findByPath("/2.html").get();
        PostNavigationResponse navigation = pageWebService.navigation(page.id);
        assertNotNull(navigation);
    }

    @Test
    public void batchGet() {
        PostResponse page = pageWebService.findByPath("/1.html").get();
        BatchGetPostRequest request = new BatchGetPostRequest();
        request.ids = Lists.newArrayList(page.id);
        List<PostResponse> pages = pageWebService.batchGet(request);
        assertEquals(1, pages.size());
    }

    @Test
    public void updatePost() {
        PostResponse page = pageWebService.findByPath("/1.html").get();
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();
        updatePostRequest.title = "test title";
        updatePostRequest.fields = Collections.singletonMap("key", "value");
        page = pageWebService.update(page.id, updatePostRequest);
        assertEquals("test title", page.title);
    }

    private CreateDraftRequest create(String path, String title, String description) {
        CreateDraftRequest request = new CreateDraftRequest();
        request.path = path;
        request.title = title;
        request.description = description;
        request.tags = Lists.newArrayList("tag");
        request.keywords = Lists.newArrayList("keyword");
        request.content = "test content";
        request.categoryId = "001";
        request.requestBy = "test";
        return request;
    }
}