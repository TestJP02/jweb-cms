package io.sited.page.rating.web;


import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.page.rating.PageRatingModuleImpl;
import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.api.rating.BatchDeleteRatingRequest;
import io.sited.page.rating.api.rating.RatingRequest;
import io.sited.page.rating.api.rating.RatingResponse;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({ServiceModule.class, PageRatingModuleImpl.class, DatabaseModule.class})
public class RatingWebServiceImplTest {
    @Inject
    MockApp mockApp;
    @Inject
    RatingWebService ratingWebService;
    private RatingResponse ratingResponse;

    @BeforeEach
    public void setup() {
        ratingResponse = ratingWebService.rate(request());
    }

    @Test
    public void get() {
        ContainerResponse httpResponse = mockApp.get("/api/page/rating/" + ratingResponse.id).execute();
        assertEquals(200, httpResponse.getStatus());
        RatingResponse response = (RatingResponse) httpResponse.getEntity();
        assertEquals(ratingResponse.id, response.id);
    }

    @Test
    public void findByPageId() {
        ContainerResponse httpResponse = mockApp.get("/api/page/rating/page/" + ratingResponse.pageId).execute();
        assertEquals(200, httpResponse.getStatus());
        Optional<RatingResponse> optional = (Optional<RatingResponse>) httpResponse.getEntity();
        assertTrue(optional.isPresent());
        assertEquals(ratingResponse.id, optional.get().id);
    }

    @Test
    public void batchGet() {
        List<String> ids = Lists.newArrayList(ratingResponse.id);
        ContainerResponse httpResponse = mockApp.put("/api/page/rating/batch-get").setEntity(ids).execute();
        assertEquals(200, httpResponse.getStatus());
        List<RatingResponse> list = (List<RatingResponse>) httpResponse.getEntity();
        assertEquals(1, list.size());
        assertEquals(ratingResponse.id, list.get(0).id);
    }

    @Test
    public void rate() {
        RatingRequest request = request();
        ContainerResponse httpResponse = mockApp.post("/api/page/rating").setEntity(request).execute();
        assertEquals(200, httpResponse.getStatus());
        RatingResponse response = (RatingResponse) httpResponse.getEntity();
        assertEquals(request.pageId, response.pageId);
        assertEquals(request.score.intValue(), response.averageScore.intValue());
        request.pageId = ratingResponse.pageId;
        request.userId = UUID.randomUUID().toString();
        request.score = 1;
        request.content = "new";
        request.ip = "127.0.0.1";
        request.requestBy = request.userId;
        httpResponse = mockApp.post("/api/page/rating").setEntity(request).execute();
        assertEquals(200, httpResponse.getStatus());
        response = (RatingResponse) httpResponse.getEntity();
        assertEquals(ratingResponse.id, response.id);
        assertEquals((5 + 1) / 2, response.averageScore.intValue());
    }

    @Test
    public void batchDelete() {
        BatchDeleteRatingRequest deleteRequest = new BatchDeleteRatingRequest();
        deleteRequest.ids = Lists.newArrayList(ratingResponse.id);
        deleteRequest.requestBy = UUID.randomUUID().toString();
        ContainerResponse httpResponse = mockApp.put("/api/page/rating/batch-delete").setEntity(deleteRequest).execute();
        assertEquals(204, httpResponse.getStatus());
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