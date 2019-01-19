package app.jweb.page.service;

import app.jweb.database.Database;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.page.api.statistics.BatchGetPageStatisticsRequest;
import app.jweb.page.api.statistics.PageStatisticsQuery;
import app.jweb.page.api.statistics.UpdatePageStatisticsRequest;
import app.jweb.page.domain.PageStatistics;
import app.jweb.page.domain.PageStatusStatistics;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageStatisticsService {
    @Inject
    Repository<PageStatistics> repository;
    @Inject
    Database database;

    public Optional<PageStatistics> findById(String pageId) {
        return repository.query("SELECT t from PageStatistics t WHERE id=?0", pageId).findOne();
    }

    public List<PageStatistics> batchGet(BatchGetPageStatisticsRequest request) {
        if (request.ids == null || request.ids.isEmpty()) {
            return ImmutableList.of();
        }
        return repository.batchGet(request.ids);
    }

    public QueryResponse<PageStatistics> find(PageStatisticsQuery query) {
        Query<PageStatistics> pageStatisticsQuery = repository.query("SELECT t from PageStatistics t");
        pageStatisticsQuery.limit(query.page, query.limit);
        if (query.sortingField != null) {
            pageStatisticsQuery.sort(query.sortingField, query.desc);
        }
        return pageStatisticsQuery.findAll();
    }
//
//    public QueryResponse<PageStatistics> find(PopularPageQuery query) {
//        Query<PageStatistics> statisticsQuery = repository.query("SELECT t from PageStatistics t");
//        if (query.type == RankType.DAILY) {
//            statisticsQuery.sort("t.totalDailyLiked", true);
//        } else if (query.type == null || query.type == RankType.WEEKLY) {
//            statisticsQuery.sort("t.totalWeeklyLiked", true);
//        } else if (query.type == RankType.MONTHLY) {
//            statisticsQuery.sort("t.totalMonthlyLiked", true);
//        } else {
//            statisticsQuery.sort("t.totalLiked", true);
//        }
//        statisticsQuery.limit(query.page, query.limit);
//        return statisticsQuery.findAll();
//    }
//
//    public QueryResponse<PageStatistics> find(TrendingPageQuery query) {
//        Query<PageStatistics> statisticsQuery = repository.query("SELECT t from PageStatistics t");
//        if (query.type == RankType.DAILY) {
//            statisticsQuery.sort("t.totalDailyVisited", true);
//        } else if (query.type == null || query.type == RankType.WEEKLY) {
//            statisticsQuery.sort("t.totalWeeklyVisited", true);
//        } else if (query.type == RankType.MONTHLY) {
//            statisticsQuery.sort("t.totalMonthlyVisited", true);
//        } else {
//            statisticsQuery.sort("t.totalVisited", true);
//        }
//        statisticsQuery.limit(query.page, query.limit);
//        return statisticsQuery.findAll();
//    }

    @Transactional
    public PageStatistics createIfNoneExist(String pageId, String categoryId, String requestBy) {
        Optional<PageStatistics> pageStatisticsOptional = repository.query("SELECT t from PageStatistics t WHERE id=?0", pageId).findOne();
        if (!pageStatisticsOptional.isPresent()) {
            PageStatistics pageStatistics = new PageStatistics();
            pageStatistics.id = pageId;
            pageStatistics.categoryId = categoryId;
            pageStatistics.totalCommented = 0;
            pageStatistics.totalVisited = 0;
            pageStatistics.totalDailyVisited = 0;
            pageStatistics.totalWeeklyVisited = 0;
            pageStatistics.totalMonthlyVisited = 0;
            pageStatistics.totalLiked = 0;
            pageStatistics.totalDailyLiked = 0;
            pageStatistics.totalWeeklyLiked = 0;
            pageStatistics.totalMonthlyLiked = 0;
            pageStatistics.totalDisliked = 0;
            pageStatistics.createdBy = requestBy;
            pageStatistics.createdTime = OffsetDateTime.now();
            pageStatistics.updatedTime = OffsetDateTime.now();
            pageStatistics.updatedBy = requestBy;

            repository.insert(pageStatistics);
            return pageStatistics;
        }
        return pageStatisticsOptional.get();
    }

    @Transactional
    public void create(PageStatistics pageStatistics) {
        repository.insert(pageStatistics);
    }

    @Transactional
    public void delete(String pageId) {
        repository.delete(pageId);
    }

    @Transactional
    public void resetDailyVisited() {
        repository.execute("UPDATE PageStatistics t SET t.totalDailyVisited=0");
    }

    @Transactional
    public void resetWeeklyVisited() {
        repository.execute("UPDATE PageStatistics t SET t.totalWeeklyVisited=0");
    }

    @Transactional
    public void resetMonthlyVisited() {
        repository.execute("UPDATE PageStatistics t SET t.totalMonthlyVisited=0");
    }

    @Transactional
    public void visit(String id, int count, Object requestBy) {
        repository.execute("UPDATE PageStatistics t SET t.totalVisited=t.totalVisited+?0,t.totalDailyVisited=t.totalDailyVisited+?0,t.totalWeeklyVisited=t.totalWeeklyVisited+?0,t.totalMonthlyVisited=t.totalMonthlyVisited+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void commentCreated(String id, int count, Object requestBy) {
        repository.execute("UPDATE PageStatistics t SET t.totalCommented=t.totalCommented+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void commentDeleted(String id, int count, Object requestBy) {
        repository.execute("UPDATE PageStatistics t SET t.totalCommented=t.totalCommented-?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void like(String id, int count, Object requestBy) {
        repository.execute("UPDATE PageStatistics t SET t.totalLiked=t.totalLiked+?0,t.totalDailyLiked=t.totalDailyLiked+?0,t.totalWeeklyLiked=t.totalWeeklyLiked+?0,t.totalMonthlyLiked=t.totalMonthlyLiked+?0, t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void dislike(String id, int count, Object requestBy) {
        repository.execute("UPDATE PageStatistics t SET t.totalDisliked=t.totalDisliked+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public PageStatistics update(String pageId, UpdatePageStatisticsRequest request) {
        PageStatistics pageStatistics = repository.get(pageId);
        if (request.categoryId != null) {
            pageStatistics.categoryId = request.categoryId;
        }
        if (request.totalVisited != null) {
            pageStatistics.totalVisited = request.totalVisited;
        }
        if (request.totalLiked != null) {
            pageStatistics.totalLiked = request.totalLiked;
        }
        if (request.totalDisliked != null) {
            pageStatistics.totalDisliked = request.totalDisliked;
        }
        if (request.totalCommented != null) {
            pageStatistics.totalCommented = request.totalCommented;
        }
        if (request.totalDailyVisited != null) {
            pageStatistics.totalDailyVisited = request.totalDailyVisited;
        }
        if (request.totalWeeklyVisited != null) {
            pageStatistics.totalWeeklyVisited = request.totalWeeklyVisited;
        }
        if (request.totalMonthlyVisited != null) {
            pageStatistics.totalMonthlyVisited = request.totalMonthlyVisited;
        }
        if (request.totalDailyLiked != null) {
            pageStatistics.totalDailyLiked = request.totalDailyLiked;
        }
        if (request.totalWeeklyLiked != null) {
            pageStatistics.totalWeeklyLiked = request.totalWeeklyLiked;
        }
        if (request.totalMonthlyLiked != null) {
            pageStatistics.totalMonthlyLiked = request.totalMonthlyLiked;
        }
        pageStatistics.updatedBy = request.requestBy;
        pageStatistics.updatedTime = OffsetDateTime.now();
        repository.update(pageId, pageStatistics);
        return pageStatistics;
    }

    public List<PageStatusStatistics> statusStatistics() {
        String sql = "SELECT t.status AS t.status, COUNT(*) AS t.total FROM Page t GROUP BY t.status";
        Query<PageStatusStatistics> query = database.query(sql, PageStatusStatistics.class);
        return query.find();
    }
}
