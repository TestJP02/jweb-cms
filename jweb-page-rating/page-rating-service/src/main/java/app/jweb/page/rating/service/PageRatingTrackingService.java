package app.jweb.page.rating.service;

import app.jweb.page.rating.api.rating.RatingRequest;
import app.jweb.page.rating.api.rating.RatingTrackingQuery;
import app.jweb.page.rating.domain.PageRatingTracking;
import com.google.common.base.Strings;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageRatingTrackingService {
    @Inject
    Repository<PageRatingTracking> trackingRepository;


    @Transactional
    public void track(RatingRequest ratingRequest) {
        PageRatingTracking tracking = new PageRatingTracking();
        tracking.id = UUID.randomUUID().toString();
        tracking.pageId = ratingRequest.pageId;
        tracking.userId = ratingRequest.userId;
        tracking.ip = ratingRequest.ip;
        tracking.score = ratingRequest.score;
        tracking.content = ratingRequest.content;
        tracking.createdTime = OffsetDateTime.now();
        tracking.createdBy = ratingRequest.requestBy;
        trackingRepository.insert(tracking);
    }


    public Optional<PageRatingTracking> findTracking(RatingRequest ratingRequest) {
        Query<PageRatingTracking> query = trackingRepository.query("SELECT t FROM PageRatingTracking t WHERE 1=1");
        int index = 0;
        query.append("AND t.pageId=?" + index++, ratingRequest.pageId);
        if (Strings.isNullOrEmpty(ratingRequest.userId)) {
            query.append("AND t.ip=?" + index, ratingRequest.ip);
        } else {
            query.append("AND t.userId=?" + index, ratingRequest.userId);
        }
        return query.findOne();
    }

    public QueryResponse<PageRatingTracking> find(RatingTrackingQuery ratingTrackingQuery) {
        Query<PageRatingTracking> query = trackingRepository.query("SELECT t FROM PageRatingTracking t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(ratingTrackingQuery.pageId)) {
            query.append("AND t.pageId=?" + index++, ratingTrackingQuery.pageId);
        }
        if (ratingTrackingQuery.startTime != null) {
            query.append("AND t.createdTime>=?" + index++, ratingTrackingQuery.startTime);
        }
        if (ratingTrackingQuery.endTime != null) {
            query.append("AND t.createdTime<=?" + index, ratingTrackingQuery.endTime);
        }
        query.limit(ratingTrackingQuery.page, ratingTrackingQuery.limit);
        return query.findAll();
    }
}
