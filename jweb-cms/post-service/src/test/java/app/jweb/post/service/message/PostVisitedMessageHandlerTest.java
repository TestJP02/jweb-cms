package app.jweb.post.service.message;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.message.MessagePublisher;
import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PostVisitedMessage;
import app.jweb.scheduler.SchedulerModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PostModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class PostVisitedMessageHandlerTest {
    @Inject
    PostDraftWebService pageDraftWebService;

    @Inject
    PostWebService pageWebService;

    @Inject
    MessagePublisher<PostVisitedMessage> publisher;

    @Test
    public void visit() throws InterruptedException {
        DraftResponse draft = pageDraftWebService.create(request());
        PostResponse page = pageDraftWebService.publish(draft.id, draft.updatedBy);
        PostVisitedMessage visitedMessage = new PostVisitedMessage();
        visitedMessage.postId = page.id;

        publisher.publish(visitedMessage);
        Thread.sleep(1000);
        PostResponse updatedPost = pageWebService.get(page.id);
        assertNotNull(updatedPost);
    }

    private CreateDraftRequest request() {
        CreateDraftRequest request = new CreateDraftRequest();
        request.path = "/path";
        request.description = "description";
        request.title = "title";
        request.tags = Lists.newArrayList();
        request.keywords = Lists.newArrayList();
        return request;
    }
}