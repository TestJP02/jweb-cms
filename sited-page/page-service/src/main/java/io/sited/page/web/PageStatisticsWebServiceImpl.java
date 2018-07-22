package io.sited.page.web;

import io.sited.page.api.PageStatisticsWebService;
import io.sited.page.api.statistics.PageStatisticsQuery;
import io.sited.page.api.statistics.PageStatisticsResponse;
import io.sited.page.domain.PageStatistics;
import io.sited.page.service.PageStatisticsService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Optional;

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
    public Optional<PageStatisticsResponse> findById(String pageId) {
        return pageStatisticsService.findById(pageId).map(this::response);
    }

    private PageStatisticsResponse response(PageStatistics pageStatistics) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.id = pageStatistics.id;
        response.categoryId = pageStatistics.categoryId;
        response.totalVisited = pageStatistics.totalVisited;
        response.totalCommented = pageStatistics.totalCommented;
        response.createdTime = pageStatistics.createdTime;
        response.createdBy = pageStatistics.createdBy;
        response.updatedTime = pageStatistics.updatedTime;
        response.updatedBy = pageStatistics.updatedBy;
        return response;
    }
}
