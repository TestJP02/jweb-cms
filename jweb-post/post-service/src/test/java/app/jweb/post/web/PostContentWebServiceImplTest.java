package app.jweb.post.web;


import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostContentWebService;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.content.PostContentResponse;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.post.PostResponse;
import app.jweb.scheduler.SchedulerModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PostModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class PostContentWebServiceImplTest {
    @Inject
    PostDraftWebService pageDraftWebService;

    @Inject
    PostContentWebService pageContentWebService;

    PostResponse page;

    @BeforeEach
    public void setup() throws InterruptedException {
        DraftResponse page1 = pageDraftWebService.create(create("/1.html", "title 1", "description 1"));
        pageDraftWebService.publish(page1.id, "test");

        DraftResponse page2 = pageDraftWebService.create(create("/2.html", "title 2", "description 2"));
        page = pageDraftWebService.publish(page2.id, "test");

        pageDraftWebService.create(create("/3.html", "title 3", "description 3"));
    }

    @Test
    public void batchGet() {
        List<PostContentResponse> contents = pageContentWebService.batchGetByPostIds(Lists.newArrayList(page.id));
        assertEquals(1, contents.size());
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