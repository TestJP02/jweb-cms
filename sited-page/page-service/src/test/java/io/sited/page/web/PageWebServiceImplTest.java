package io.sited.page.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.draft.UpdateDraftRequest;
import io.sited.page.api.page.LatestQuery;
import io.sited.page.api.page.PageNavigationResponse;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
public class PageWebServiceImplTest {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageDraftWebService pageDraftWebService;

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
        PageResponse page = pageWebService.findByPath("/1.html").get();
        CreateDraftRequest request = new CreateDraftRequest();
        request.path = page.path;
        request.pageId = page.id;
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

        PageResponse updatedPage = pageWebService.findByPath("/1.html").get();
        assertEquals(page.id, updatedPage.id);
        assertEquals("title 3", updatedPage.title);
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

        PageResponse updatedPage = pageWebService.findByPath("/3.html").get();
        assertEquals("title 3", updatedPage.title);
    }

    @Test
    public void findRelated() {
        PageResponse page = pageWebService.findByPath("/1.html").get();
        PageRelatedQuery query = new PageRelatedQuery();
        query.id = page.id;
        query.limit = 5;

        List<PageResponse> related = pageWebService.findRelated(query);
        assertEquals(1, related.size());
        assertNotEquals(page.id, related.get(0).id);
    }

    @Test
    public void latest() throws Exception {
        LatestQuery latestQuery = new LatestQuery();
        latestQuery.limit = 1;
        List<PageResponse> latest = pageWebService.latest(latestQuery);
        assertEquals(1, latest.size());
    }

    @Test
    public void navigation() {
        PageResponse page = pageWebService.findByPath("/2.html").get();
        PageNavigationResponse navigation = pageWebService.navigation(page.id);
        assertNotNull(navigation);
    }

    @Test
    public void batchGet() {
        PageResponse page = pageWebService.findByPath("/1.html").get();
        List<PageResponse> pages = pageWebService.batchGet(Lists.newArrayList(page.id));
        assertEquals(1, pages.size());
    }

    private CreateDraftRequest create(String path, String title, String description) throws InterruptedException {
        Thread.sleep(1000);
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