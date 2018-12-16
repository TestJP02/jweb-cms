package app.jweb.post.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.post.api.post.PopularPostQuery;
import app.jweb.post.api.post.RankType;
import app.jweb.post.api.post.TrendingPostQuery;
import app.jweb.post.api.statistics.BatchGetPostStatisticsRequest;
import app.jweb.post.api.statistics.PostStatisticsQuery;
import app.jweb.post.api.statistics.UpdatePostStatisticsRequest;
import app.jweb.post.domain.PostStatistics;
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
public class PostStatisticsService {
    @Inject
    Repository<PostStatistics> repository;

    public Optional<PostStatistics> findById(String postId) {
        return repository.query("SELECT t from PostStatistics t WHERE id=?0", postId).findOne();
    }

    public List<PostStatistics> batchGet(BatchGetPostStatisticsRequest request) {
        if (request.ids == null || request.ids.isEmpty()) {
            return ImmutableList.of();
        }
        return repository.batchGet(request.ids);
    }

    public QueryResponse<PostStatistics> find(PostStatisticsQuery query) {
        Query<PostStatistics> postStatisticsQuery = repository.query("SELECT t from PostStatistics t");
        postStatisticsQuery.limit(query.page, query.limit);
        if (query.sortingField != null) {
            postStatisticsQuery.sort(query.sortingField, query.desc);
        }
        return postStatisticsQuery.findAll();
    }

    public QueryResponse<PostStatistics> find(PopularPostQuery query) {
        Query<PostStatistics> statisticsQuery = repository.query("SELECT t from PostStatistics t");
        if (query.type == RankType.DAILY) {
            statisticsQuery.sort("t.totalDailyLiked", true);
        } else if (query.type == null || query.type == RankType.WEEKLY) {
            statisticsQuery.sort("t.totalWeeklyLiked", true);
        } else if (query.type == RankType.MONTHLY) {
            statisticsQuery.sort("t.totalMonthlyLiked", true);
        } else {
            statisticsQuery.sort("t.totalLiked", true);
        }
        statisticsQuery.limit(query.page, query.limit);
        return statisticsQuery.findAll();
    }

    public QueryResponse<PostStatistics> find(TrendingPostQuery query) {
        Query<PostStatistics> statisticsQuery = repository.query("SELECT t from PostStatistics t");
        if (query.type == RankType.DAILY) {
            statisticsQuery.sort("t.totalDailyVisited", true);
        } else if (query.type == null || query.type == RankType.WEEKLY) {
            statisticsQuery.sort("t.totalWeeklyVisited", true);
        } else if (query.type == RankType.MONTHLY) {
            statisticsQuery.sort("t.totalMonthlyVisited", true);
        } else {
            statisticsQuery.sort("t.totalVisited", true);
        }
        statisticsQuery.limit(query.page, query.limit);
        return statisticsQuery.findAll();
    }

    @Transactional
    public PostStatistics createIfNoneExist(String postId, String categoryId, String requestBy) {
        Optional<PostStatistics> postStatisticsOptional = repository.query("SELECT t from PostStatistics t WHERE id=?0", postId).findOne();
        if (!postStatisticsOptional.isPresent()) {
            PostStatistics postStatistics = new PostStatistics();
            postStatistics.id = postId;
            postStatistics.categoryId = categoryId;
            postStatistics.totalCommented = 0;
            postStatistics.totalVisited = 0;
            postStatistics.totalDailyVisited = 0;
            postStatistics.totalWeeklyVisited = 0;
            postStatistics.totalMonthlyVisited = 0;
            postStatistics.totalLiked = 0;
            postStatistics.totalDailyLiked = 0;
            postStatistics.totalWeeklyLiked = 0;
            postStatistics.totalMonthlyLiked = 0;
            postStatistics.totalDisliked = 0;
            postStatistics.createdBy = requestBy;
            postStatistics.createdTime = OffsetDateTime.now();
            postStatistics.updatedTime = OffsetDateTime.now();
            postStatistics.updatedBy = requestBy;

            repository.insert(postStatistics);
            return postStatistics;
        }
        return postStatisticsOptional.get();
    }

    @Transactional
    public void create(PostStatistics postStatistics) {
        repository.insert(postStatistics);
    }

    @Transactional
    public void delete(String postId) {
        repository.delete(postId);
    }

    @Transactional
    public void resetDailyVisited() {
        repository.execute("UPDATE PostStatistics t SET t.totalDailyVisited=0");
    }

    @Transactional
    public void resetWeeklyVisited() {
        repository.execute("UPDATE PostStatistics t SET t.totalWeeklyVisited=0");
    }

    @Transactional
    public void resetMonthlyVisited() {
        repository.execute("UPDATE PostStatistics t SET t.totalMonthlyVisited=0");
    }

    @Transactional
    public void visit(String id, int count, Object requestBy) {
        repository.execute("UPDATE PostStatistics t SET t.totalVisited=t.totalVisited+?0,t.totalDailyVisited=t.totalDailyVisited+?0,t.totalWeeklyVisited=t.totalWeeklyVisited+?0,t.totalMonthlyVisited=t.totalMonthlyVisited+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void commentCreated(String id, int count, Object requestBy) {
        repository.execute("UPDATE PostStatistics t SET t.totalCommented=t.totalCommented+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void commentDeleted(String id, int count, Object requestBy) {
        repository.execute("UPDATE PostStatistics t SET t.totalCommented=t.totalCommented-?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void like(String id, int count, Object requestBy) {
        repository.execute("UPDATE PostStatistics t SET t.totalLiked=t.totalLiked+?0,t.totalDailyLiked=t.totalDailyLiked+?0,t.totalWeeklyLiked=t.totalWeeklyLiked+?0,t.totalMonthlyLiked=t.totalMonthlyLiked+?0, t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public void dislike(String id, int count, Object requestBy) {
        repository.execute("UPDATE PostStatistics t SET t.totalDisliked=t.totalDisliked+?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.id=?3", count, OffsetDateTime.now(), requestBy, id);
    }

    @Transactional
    public PostStatistics update(String postId, UpdatePostStatisticsRequest request) {
        PostStatistics postStatistics = repository.get(postId);
        if (request.categoryId != null) {
            postStatistics.categoryId = request.categoryId;
        }
        if (request.totalVisited != null) {
            postStatistics.totalVisited = request.totalVisited;
        }
        if (request.totalLiked != null) {
            postStatistics.totalLiked = request.totalLiked;
        }
        if (request.totalDisliked != null) {
            postStatistics.totalDisliked = request.totalDisliked;
        }
        if (request.totalCommented != null) {
            postStatistics.totalCommented = request.totalCommented;
        }
        if (request.totalDailyVisited != null) {
            postStatistics.totalDailyVisited = request.totalDailyVisited;
        }
        if (request.totalWeeklyVisited != null) {
            postStatistics.totalWeeklyVisited = request.totalWeeklyVisited;
        }
        if (request.totalMonthlyVisited != null) {
            postStatistics.totalMonthlyVisited = request.totalMonthlyVisited;
        }
        if (request.totalDailyLiked != null) {
            postStatistics.totalDailyLiked = request.totalDailyLiked;
        }
        if (request.totalWeeklyLiked != null) {
            postStatistics.totalWeeklyLiked = request.totalWeeklyLiked;
        }
        if (request.totalMonthlyLiked != null) {
            postStatistics.totalMonthlyLiked = request.totalMonthlyLiked;
        }
        postStatistics.updatedBy = request.requestBy;
        postStatistics.updatedTime = OffsetDateTime.now();
        repository.update(postId, postStatistics);
        return postStatistics;
    }
}
