package io.sited.page.admin.web.api;

import io.sited.page.api.PageStatisticsWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.statistics.PageStatisticsQuery;
import io.sited.page.api.statistics.PageStatisticsResponse;
import io.sited.util.collection.QueryResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/page/statistics")
public class PageStatisticsAdminController {
    @Inject
    PageStatisticsWebService pageStatisticsWebService;

    @Inject
    PageWebService pageWebService;

    @RolesAllowed("LIST")
    @Path("/most-visited")
    @PUT
    public QueryResponse<PageStatisticsAdminResponse> tree(PageStatisticsQuery query) {
        QueryResponse<PageStatisticsResponse> statisticsResponses = pageStatisticsWebService.find(query);
        List<String> pageIds = statisticsResponses.items.stream().map(item -> item.id).collect(Collectors.toList());
        Map<String, PageResponse> pages = pageWebService.batchGet(pageIds).stream().collect(Collectors.toMap(page -> page.id, page -> page));
        QueryResponse<PageStatisticsAdminResponse> adminResponses = new QueryResponse<>();
        adminResponses.total = statisticsResponses.total;
        adminResponses.limit = statisticsResponses.limit;
        adminResponses.page = statisticsResponses.page;
        adminResponses.items = statisticsResponses.items.stream().map(item -> {
            PageStatisticsAdminResponse response = new PageStatisticsAdminResponse();
            PageResponse page = pages.get(item.id);
            response.path = page.path;
            response.title = page.title;
            response.totalCommented = item.totalCommented;
            response.totalVisited = item.totalVisited;
            response.totalDailyVisited = item.totalDailyVisited;
            response.totalWeeklyVisited = item.totalWeeklyVisited;
            response.totalMonthlyVisited = item.totalMonthlyVisited;
            return response;
        }).collect(Collectors.toList());
        return adminResponses;
    }
}
