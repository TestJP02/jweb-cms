package app.jweb.post.web;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.PostStatisticsWebService;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.statistics.BatchGetPostStatisticsRequest;
import app.jweb.post.api.statistics.PostStatisticsResponse;
import app.jweb.post.api.statistics.UpdatePostStatisticsRequest;
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
class PostStatisticsWebServiceImplTest {
    @Inject
    PostStatisticsWebService pageStatisticsWebService;
    @Inject
    PostDraftWebService pageDraftWebService;
    PostResponse pageResponse;

    @BeforeEach
    public void setup() throws InterruptedException {
        DraftResponse page = pageDraftWebService.create(create("/1.html", "title 1", "description 1"));
        pageResponse = pageDraftWebService.publish(page.id, "test");
        Thread.sleep(2000);
    }

    @Test
    void batchGet() {
        BatchGetPostStatisticsRequest request = new BatchGetPostStatisticsRequest();
        request.ids = Lists.newArrayList(pageResponse.id);

        List<PostStatisticsResponse> results = pageStatisticsWebService.batchGet(request);
        assertEquals(1, results.size());
    }

    @Test
    void update() {
        UpdatePostStatisticsRequest request = new UpdatePostStatisticsRequest();
        request.totalCommented = 100;
        request.totalLiked = 100;
        request.totalDisliked = 100;
        request.totalVisited = 100;

        pageStatisticsWebService.update(pageResponse.id, request);
        PostStatisticsResponse response = pageStatisticsWebService.findById(pageResponse.id).orElseThrow();
        assertEquals(100, (int) response.totalCommented);
        assertEquals(100, (int) response.totalLiked);
        assertEquals(100, (int) response.totalDisliked);
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