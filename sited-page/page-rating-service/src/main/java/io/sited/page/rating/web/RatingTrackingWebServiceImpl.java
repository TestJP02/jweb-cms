package io.sited.page.rating.web;

import io.sited.page.rating.api.RatingTrackingWebService;
import io.sited.page.rating.api.rating.RatingTrackingQuery;
import io.sited.page.rating.api.rating.RatingTrackingResponse;
import io.sited.page.rating.domain.PageRatingTracking;
import io.sited.page.rating.service.PageRatingService;
import io.sited.page.rating.service.PageRatingTrackingService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;

/**
 * @author chi
 */
public class RatingTrackingWebServiceImpl implements RatingTrackingWebService {
    @Inject
    PageRatingService pageRatingService;

    @Inject
    PageRatingTrackingService pageRatingTrackingService;

    @Override
    public QueryResponse<RatingTrackingResponse> find(RatingTrackingQuery query) {
        return pageRatingTrackingService.find(query).map(this::response);
    }

    private RatingTrackingResponse response(PageRatingTracking tracking) {
        RatingTrackingResponse response = new RatingTrackingResponse();
        response.id = tracking.id;
        response.pageId = tracking.pageId;
        response.userId = tracking.userId;
        response.ip = tracking.ip;
        response.score = tracking.score;
        response.content = tracking.content;
        response.createdTime = tracking.createdTime;
        response.createdBy = tracking.createdBy;
        return response;
    }
}
