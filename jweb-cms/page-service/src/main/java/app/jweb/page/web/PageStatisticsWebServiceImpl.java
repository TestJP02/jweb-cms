package app.jweb.page.web;

import app.jweb.page.api.PageStatisticsWebService;
import app.jweb.page.api.statistics.BatchGetPageStatisticsRequest;
import app.jweb.page.api.statistics.PageStatisticsQuery;
import app.jweb.page.api.statistics.PageStatisticsResponse;
import app.jweb.page.api.statistics.UpdatePageStatisticsRequest;
import app.jweb.page.domain.PageStatistics;
import app.jweb.page.service.PageStatisticsService;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageStatisticsWebServiceImpl implements PageStatisticsWebService {
    @Inject
    PageStatisticsService pageStatisticsService;

    @Override
    public QueryResponse<PageStatisticsResponse> find(PageStatisticsQuery query) {
        return pageStatisticsService.find(query).map(this::response);
    }

    @Override
    public List<PageStatisticsResponse> batchGet(BatchGetPageStatisticsRequest request) {
        List<PageStatistics> list = pageStatisticsService.batchGet(request);
        return list.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public Optional<PageStatisticsResponse> findById(String pageId) {
        return pageStatisticsService.findById(pageId).map(this::response);
    }

    @Override
    public PageStatisticsResponse update(String pageId, UpdatePageStatisticsRequest request) {
        PageStatistics pageStatistics = pageStatisticsService.update(pageId, request);
        return response(pageStatistics);
    }

    private PageStatisticsResponse response(PageStatistics pageStatistics) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.id = pageStatistics.id;
        response.categoryId = pageStatistics.categoryId;
        response.totalVisited = pageStatistics.totalVisited;
        response.totalDailyVisited = pageStatistics.totalDailyVisited;
        response.totalWeeklyVisited = pageStatistics.totalWeeklyVisited;
        response.totalMonthlyVisited = pageStatistics.totalMonthlyVisited;
        response.totalLiked = pageStatistics.totalLiked;
        response.totalDailyLiked = pageStatistics.totalDailyLiked;
        response.totalWeeklyLiked = pageStatistics.totalWeeklyLiked;
        response.totalMonthlyLiked = pageStatistics.totalMonthlyLiked;
        response.totalCommented = pageStatistics.totalCommented;
        response.totalDisliked = pageStatistics.totalDisliked;
        response.createdTime = pageStatistics.createdTime;
        response.createdBy = pageStatistics.createdBy;
        response.updatedTime = pageStatistics.updatedTime;
        response.updatedBy = pageStatistics.updatedBy;
        return response;
    }
}
