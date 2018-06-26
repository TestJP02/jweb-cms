package io.sited.page.rating.service;


import io.sited.database.Repository;
import io.sited.page.rating.api.rating.RatingRequest;
import io.sited.page.rating.domain.PageRating;
import io.sited.util.exception.Exceptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageRatingService {
    @Inject
    Repository<PageRating> repository;

    @Inject
    PageRatingTrackingService pageRatingTrackingService;

    @Transactional
    public PageRating rate(RatingRequest ratingRequest) {
        Optional<PageRating> optional = findByPageId(ratingRequest.pageId);
        int index = 0;
        if (optional.isPresent()) {
            if (pageRatingTrackingService.findTracking(ratingRequest).isPresent()) return optional.get();
            StringBuilder sql = new StringBuilder(64);
            sql.append("UPDATE PageRating SET totalScore=totalScore+?").append(index++).append(",totalScored=totalScored+1,updatedTime=?").append(index++).append(",updated_by=?").append(index++).append(',');
            switch (ratingRequest.score) {
                case 1:
                    sql.append("totalScored1=totalScored1+1 ");
                    break;
                case 2:
                    sql.append("totalScored2=totalScored2+1 ");
                    break;
                case 3:
                    sql.append("totalScored3=totalScored3+1 ");
                    break;
                case 4:
                    sql.append("totalScored4=totalScored4+1 ");
                    break;
                case 5:
                    sql.append("totalScored5=totalScored5+1 ");
                    break;
                default:
                    throw Exceptions.badRequestException("rating.score", "invalid score");
            }
            sql.append("WHERE id=?").append(index);
            repository.execute(sql.toString(), ratingRequest.score, OffsetDateTime.now(), ratingRequest.requestBy, optional.get().id);
            pageRatingTrackingService.track(ratingRequest);
            return get(optional.get().id);
        } else {
            return createRating(ratingRequest);
        }
    }

    @Transactional
    private PageRating createRating(RatingRequest ratingRequest) {
        PageRating rating = new PageRating();
        rating.id = UUID.randomUUID().toString();
        rating.pageId = ratingRequest.pageId;
        rating.totalScore = ratingRequest.score;
        rating.totalScored = 1;
        switch (ratingRequest.score) {
            case 1:
                rating.totalScored1 = 1;
                rating.totalScored2 = 0;
                rating.totalScored3 = 0;
                rating.totalScored4 = 0;
                rating.totalScored5 = 0;
                break;
            case 2:
                rating.totalScored1 = 0;
                rating.totalScored2 = 1;
                rating.totalScored3 = 0;
                rating.totalScored4 = 0;
                rating.totalScored5 = 0;
                break;
            case 3:
                rating.totalScored1 = 0;
                rating.totalScored2 = 0;
                rating.totalScored3 = 1;
                rating.totalScored4 = 0;
                rating.totalScored5 = 0;
                break;
            case 4:
                rating.totalScored1 = 0;
                rating.totalScored2 = 0;
                rating.totalScored3 = 0;
                rating.totalScored4 = 1;
                rating.totalScored5 = 0;
                break;
            case 5:
                rating.totalScored1 = 0;
                rating.totalScored2 = 0;
                rating.totalScored3 = 0;
                rating.totalScored4 = 0;
                rating.totalScored5 = 1;
                break;
            default:
                throw Exceptions.badRequestException("rating.score", "invalid score");
        }
        rating.createdTime = OffsetDateTime.now();
        rating.updatedTime = OffsetDateTime.now();
        rating.createdBy = ratingRequest.requestBy;
        rating.updatedBy = ratingRequest.requestBy;
        repository.insert(rating);
        pageRatingTrackingService.track(ratingRequest);
        return rating;
    }

    public PageRating get(String id) {
        return repository.get(id);
    }

    public Optional<PageRating> findByPageId(String pageId) {
        return repository.query("SELECT t FROM PageRating t WHERE t.pageId=?0", pageId).findOne();
    }

    public List<PageRating> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }


    @Transactional
    public void batchDelete(List<String> ids) {
        repository.batchDelete(ids);
        //todo delete tracking?
    }
}
