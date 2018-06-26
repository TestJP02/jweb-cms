package io.sited.page.tracking.web;

import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.tracking.PageTrackingModuleImpl;
import io.sited.page.tracking.api.tracking.PageStatisticsQuery;
import io.sited.page.tracking.api.tracking.PageStatisticsResponse;
import io.sited.page.tracking.api.tracking.PageTrackingType;
import io.sited.page.tracking.service.PageTrackingService;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageTrackingModuleImpl.class, DatabaseModule.class, MessageModule.class,
    ServiceModule.class, PageModuleImpl.class})
public class PageTrackingStatisticsWebServiceImplTest {
    @Inject
    MockApp mockApp;
    @Inject
    PageTrackingService pageTrackingService;

    @BeforeEach
    public void setup() {
        PageVisitedMessage pageVisitedMessage = new PageVisitedMessage();
        pageVisitedMessage.pageId = UUID.randomUUID().toString();
        pageVisitedMessage.categoryId = UUID.randomUUID().toString();
        pageVisitedMessage.userId = UUID.randomUUID().toString();
        pageVisitedMessage.clientId = UUID.randomUUID().toString();
        pageVisitedMessage.timestamp = OffsetDateTime.now();
        pageVisitedMessage.requestBy = pageVisitedMessage.userId;
        pageTrackingService.track(pageVisitedMessage);
    }

    @Test
    public void find() {
        PageStatisticsQuery pageStatisticsQuery = new PageStatisticsQuery();
        pageStatisticsQuery.type = PageTrackingType.DAILY;
        ContainerResponse httpResponse = mockApp.put("/api/page/tracking/statistics").setEntity(pageStatisticsQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        List<PageStatisticsResponse> list = (List<PageStatisticsResponse>) httpResponse.getEntity();
        assertEquals(1, list.size());

        pageStatisticsQuery.type = PageTrackingType.WEEKLY;
        httpResponse = mockApp.put("/api/page/tracking/statistics").setEntity(pageStatisticsQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        list = (List<PageStatisticsResponse>) httpResponse.getEntity();
        assertEquals(1, list.size());

        pageStatisticsQuery.type = PageTrackingType.MONTHLY;
        httpResponse = mockApp.put("/api/page/tracking/statistics").setEntity(pageStatisticsQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        list = (List<PageStatisticsResponse>) httpResponse.getEntity();
        assertEquals(1, list.size());

        pageStatisticsQuery.type = PageTrackingType.OVERALL;
        httpResponse = mockApp.put("/api/page/tracking/statistics").setEntity(pageStatisticsQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        list = (List<PageStatisticsResponse>) httpResponse.getEntity();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).total.intValue());
    }
}