package io.sited.page.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageCommentWebService;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.comment.CommentResponse;
import io.sited.page.api.comment.CreateCommentRequest;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
class PageCommentWebServiceImplTest {
    @Inject
    PageCommentWebService pageCommentWebService;
    @Inject
    PageDraftWebService pageDraftWebService;
    @Inject
    PageWebService pageWebService;

    PageResponse page;

    @BeforeEach
    public void setup() throws InterruptedException {
        DraftResponse draft = pageDraftWebService.create(create("/1.html", "title 1", "description 1"));
        page = pageDraftWebService.publish(draft.id, "test");
    }

    @Test
    void create() throws InterruptedException {
        CreateCommentRequest request = new CreateCommentRequest();
        request.pageId = page.id;
        request.parentId = null;
        request.content = "some";
        request.requestBy = "SYS";

        CommentResponse comment = pageCommentWebService.create(request);
        assertNotNull(comment.id);

        Thread.sleep(1000);
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