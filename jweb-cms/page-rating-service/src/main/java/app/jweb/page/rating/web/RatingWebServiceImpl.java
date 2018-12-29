package app.jweb.page.rating.web;

import app.jweb.page.rating.api.RatingWebService;
import app.jweb.page.rating.api.rating.BatchDeleteRatingRequest;
import app.jweb.page.rating.api.rating.RatingRequest;
import app.jweb.page.rating.api.rating.RatingResponse;
import app.jweb.page.rating.domain.PageRating;
import app.jweb.page.rating.service.PageRatingService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class RatingWebServiceImpl implements RatingWebService {
    @Inject
    PageRatingService pageRatingService;

    @Override
    public RatingResponse get(String id) {
        return response(pageRatingService.get(id));
    }

    @Override
    public Optional<RatingResponse> findByPageId(String pageId) {
        return pageRatingService.findByPageId(pageId).map(this::response);
    }

    @Override
    public List<RatingResponse> batchGet(List<String> ids) {
        return pageRatingService.batchGet(ids).stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public RatingResponse rate(RatingRequest request) {
        return response(pageRatingService.rate(request));
    }

    @Override
    public void batchDelete(BatchDeleteRatingRequest request) {
        pageRatingService.batchDelete(request.ids);
    }

    private RatingResponse response(PageRating rating) {
        RatingResponse response = new RatingResponse();
        response.id = rating.id;
        response.pageId = rating.pageId;
        response.averageScore = (double) rating.totalScore / (double) rating.totalScored;
        response.totalScored = rating.totalScored;
        response.totalScored1 = rating.totalScored1;
        response.totalScored2 = rating.totalScored2;
        response.totalScored3 = rating.totalScored3;
        response.totalScored4 = rating.totalScored4;
        response.totalScored5 = rating.totalScored5;
        response.createdTime = rating.createdTime;
        response.createdBy = rating.createdBy;
        response.updatedTime = rating.updatedTime;
        response.updatedBy = rating.updatedBy;
        return response;
    }
}
