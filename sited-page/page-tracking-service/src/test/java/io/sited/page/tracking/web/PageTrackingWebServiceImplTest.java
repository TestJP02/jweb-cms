package io.sited.page.tracking.web;

import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.tracking.PageTrackingModuleImpl;
import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
import io.sited.page.tracking.api.tracking.PageTrackingType;
import io.sited.page.tracking.service.PageTrackingService;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.util.collection.QueryResponse;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageTrackingModuleImpl.class, DatabaseModule.class, MessageModule.class,
    ServiceModule.class, PageModuleImpl.class})
public class PageTrackingWebServiceImplTest {
    @Inject
    MockApp mockApp;
    @Inject
    PageTrackingService pageTrackingService;
    @Inject
    PageDraftWebService draftWebService;
    private String pageOneId;
    private String pageTwoId;

    @BeforeEach
    public void setup() throws Exception {
        PageResponse pageResponse = createPage();
        pageOneId = pageResponse.id;
        PageVisitedMessage pageVisitedMessage = new PageVisitedMessage();
        pageVisitedMessage.pageId = pageOneId;
        pageVisitedMessage.categoryId = pageResponse.categoryId;
        pageVisitedMessage.userId = UUID.randomUUID().toString();
        pageVisitedMessage.clientId = UUID.randomUUID().toString();
        pageVisitedMessage.timestamp = OffsetDateTime.now();
        pageVisitedMessage.requestBy = pageVisitedMessage.userId;
        pageTrackingService.track(pageVisitedMessage);
        pageResponse = createPage();
        pageTwoId = pageResponse.id;
        pageVisitedMessage.pageId = pageTwoId;
        pageVisitedMessage.categoryId = pageResponse.categoryId;
        pageTrackingService.track(pageVisitedMessage);
        //Thread.sleep(2000);
        pageTrackingService.track(pageVisitedMessage);
    }

    @Test
    public void find() {
        PageTrackingQuery pageTrackingQuery = new PageTrackingQuery();
        pageTrackingQuery.type = PageTrackingType.DAILY;
        ContainerResponse httpResponse = mockApp.put("/api/page/tracking/most-visit").setEntity(pageTrackingQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        QueryResponse<PageTrackingResponse> queryResponse = (QueryResponse) httpResponse.getEntity();
        assertEquals(2, queryResponse.total.longValue());
        assertEquals(2, queryResponse.items.size());
        assertEquals(pageTwoId, queryResponse.items.get(0).pageId);

        pageTrackingQuery.type = PageTrackingType.WEEKLY;
        httpResponse = mockApp.put("/api/page/tracking/most-visit").setEntity(pageTrackingQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        queryResponse = (QueryResponse) httpResponse.getEntity();
        assertEquals(2, queryResponse.total.longValue());
        assertEquals(2, queryResponse.items.size());
        assertEquals(pageTwoId, queryResponse.items.get(0).pageId);

        pageTrackingQuery.type = PageTrackingType.MONTHLY;
        httpResponse = mockApp.put("/api/page/tracking/most-visit").setEntity(pageTrackingQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        queryResponse = (QueryResponse) httpResponse.getEntity();
        assertEquals(2, queryResponse.total.longValue());
        assertEquals(2, queryResponse.items.size());
        assertEquals(pageTwoId, queryResponse.items.get(0).pageId);

        pageTrackingQuery.type = PageTrackingType.OVERALL;
        httpResponse = mockApp.put("/api/page/tracking/most-visit").setEntity(pageTrackingQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        queryResponse = (QueryResponse) httpResponse.getEntity();
        assertEquals(2, queryResponse.total.longValue());
        assertEquals(2, queryResponse.items.size());
        assertEquals(pageTwoId, queryResponse.items.get(0).pageId);
    }

    private PageResponse createPage() {
        CreateDraftRequest createDraftRequest = new CreateDraftRequest();
        createDraftRequest.categoryId = UUID.randomUUID().toString();
        createDraftRequest.path = UUID.randomUUID().toString();
        createDraftRequest.content = "";
        createDraftRequest.title = UUID.randomUUID().toString();
        createDraftRequest.imageURL = UUID.randomUUID().toString();
        createDraftRequest.userId = UUID.randomUUID().toString();
        createDraftRequest.requestBy = createDraftRequest.userId;
        DraftResponse draftResponse = draftWebService.create(createDraftRequest);
        return draftWebService.publish(draftResponse.id, createDraftRequest.requestBy);
    }
}