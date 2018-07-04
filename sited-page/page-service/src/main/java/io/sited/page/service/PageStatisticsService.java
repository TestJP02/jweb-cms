package io.sited.page.service;

import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.api.page.PopularPageQuery;
import io.sited.page.api.page.RankType;
import io.sited.page.api.statistics.PageStatisticsQuery;
import io.sited.page.domain.PageStatistics;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * @author chi
 */
public class PageStatisticsService {
    @Inject
    Repository<PageStatistics> repository;

    public Optional<PageStatistics> findById(String pageId) {
        return repository.query("SELECT t from PageStatistics t WHERE id=?0", pageId).findOne();
    }

    public QueryResponse<PageStatistics> find(PageStatisticsQuery query) {
        Query<PageStatistics> pageStatisticsQuery = repository.query("SELECT t from PageStatistics t");
        pageStatisticsQuery.limit(query.page, query.limit);
        if (query.sortingField != null) {
            pageStatisticsQuery.sort(query.sortingField, query.desc);
        }
        return pageStatisticsQuery.findAll();
    }

    public QueryResponse<PageStatistics> find(PopularPageQuery query) {
        Query<PageStatistics> statisticsQuery = repository.query("SELECT t from PageStatistics t");
        if (query.type == null || query.type == RankType.DAILY) {
            statisticsQuery.append("ORDER BY t.totalDailyVisited");
        } else if (query.type == RankType.WEEKLY) {
            statisticsQuery.append("ORDER BY t.totalWeeklyVisited");
        } else if (query.type == RankType.MONTHLY) {
            statisticsQuery.append("ORDER BY t.totalMonthlyVisited");
        }
        statisticsQuery.limit(query.page, query.limit);
        return statisticsQuery.findAll();
    }

    @Transactional
    public PageStatistics createIfNoneExist(String pageId, String requestBy) {
        Optional<PageStatistics> pageStatisticsOptional = repository.query("SELECT t from PageStatistics t WHERE id=?0", pageId).findOne();
        if (!pageStatisticsOptional.isPresent()) {
            PageStatistics pageStatistics = new PageStatistics();
            pageStatistics.id = pageId;
            pageStatistics.totalCommented = 0;
            pageStatistics.totalVisited = 0;
            pageStatistics.totalDailyVisited = 0;
            pageStatistics.totalWeeklyVisited = 0;
            pageStatistics.totalMonthlyVisited = 0;
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

}
