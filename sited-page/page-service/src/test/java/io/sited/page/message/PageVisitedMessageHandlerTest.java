package io.sited.page.message;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.message.MessagePublisher;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
public class PageVisitedMessageHandlerTest {
    @Inject
    PageDraftWebService pageDraftWebService;

    @Inject
    PageWebService pageWebService;

    @Inject
    MessagePublisher<PageVisitedMessage> publisher;

    @Test
    public void visit() throws InterruptedException {

        DraftResponse draft = pageDraftWebService.create(request());
        PageResponse page = pageDraftWebService.publish(draft.id, draft.updatedBy);
        PageVisitedMessage visitedMessage = new PageVisitedMessage();
        visitedMessage.pageId = page.id;

        publisher.publish(visitedMessage);

        Thread.sleep(1000);
        PageResponse updatedPage = pageWebService.get(page.id);
        assertEquals(1, (int) updatedPage.totalVisited);
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