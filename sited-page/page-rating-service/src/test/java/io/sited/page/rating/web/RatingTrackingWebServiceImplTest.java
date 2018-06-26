package io.sited.page.rating.web;


import io.sited.database.DatabaseModule;
import io.sited.page.rating.PageRatingModuleImpl;
import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.api.rating.RatingRequest;
import io.sited.page.rating.api.rating.RatingResponse;
import io.sited.page.rating.api.rating.RatingTrackingQuery;
import io.sited.page.rating.api.rating.RatingTrackingResponse;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, PageRatingModuleImpl.class, DatabaseModule.class})
public class RatingTrackingWebServiceImplTest {
    @Inject
    MockApp mockApp;
    @Inject
    RatingWebService ratingWebService;
    private RatingResponse ratingResponse;
    private RatingRequest request;

    @BeforeEach
    public void setup() {
        request = request();
        ratingResponse = ratingWebService.rate(request);
    }

    @Test
    public void find() {
        RatingTrackingQuery ratingTrackingQuery = new RatingTrackingQuery();
        ratingTrackingQuery.pageId = ratingResponse.pageId;
        OffsetDateTime now = OffsetDateTime.now();
        ratingTrackingQuery.startTime = now.minusDays(1);
        ratingTrackingQuery.endTime = now.plusDays(1);
        ContainerResponse httpResponse = mockApp.put("/api/rating/tracking/find").setEntity(ratingTrackingQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        QueryResponse<RatingTrackingResponse> queryResponse = (QueryResponse<RatingTrackingResponse>) httpResponse.getEntity();
        List<RatingTrackingResponse> items = queryResponse.items;
        assertEquals(1, items.size());
        RatingTrackingResponse response = items.get(0);
        assertEquals(ratingResponse.pageId, response.pageId);
        assertEquals(request.userId, response.userId);
    }

    private RatingRequest request() {
        RatingRequest request = new RatingRequest();
        request.pageId = UUID.randomUUID().toString();
        request.userId = UUID.randomUUID().toString();
        request.ip = "127.0.0.1";
        request.score = 5;
        request.content = "test";
        request.requestBy = request.userId;
        return request;
    }
}