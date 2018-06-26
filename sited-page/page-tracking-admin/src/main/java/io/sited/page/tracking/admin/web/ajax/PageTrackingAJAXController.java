package io.sited.page.tracking.admin.web.ajax;

import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageResponse;
import io.sited.page.tracking.admin.web.ajax.tracking.PageTrackingAdminResponse;
import io.sited.page.tracking.api.PageTrackingStatisticsWebService;
import io.sited.page.tracking.api.PageTrackingWebService;
import io.sited.page.tracking.api.tracking.PageStatisticsQuery;
import io.sited.page.tracking.api.tracking.PageStatisticsResponse;
import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
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
@Path("/admin/api/page/tracking")
public class PageTrackingAJAXController {
    @Inject
    PageTrackingStatisticsWebService pageTrackingStatisticsWebService;

    @Inject
    PageTrackingWebService pageTrackingWebService;

    @Inject
    PageWebService pageWebService;

    @RolesAllowed("GET")
    @Path("/visit")
    @PUT
    public List<PageStatisticsResponse> visit(PageStatisticsQuery pageStatisticsQuery) {
        return pageTrackingStatisticsWebService.find(pageStatisticsQuery);
    }

    @RolesAllowed("GET")
    @Path("/most-visit")
    @PUT
    public QueryResponse<PageTrackingAdminResponse> mostVisit(PageTrackingQuery query) {
        QueryResponse<PageTrackingResponse> responses = pageTrackingWebService.find(query);
        List<String> pageIds = responses.items.stream().map(tracking -> tracking.pageId).collect(Collectors.toList());
        Map<String, PageResponse> pages = pageWebService.batchGet(pageIds).stream().collect(Collectors.toMap(page -> page.id, page -> page));
        return responses.map(tracking -> response(tracking, pages));
    }

    private PageTrackingAdminResponse response(PageTrackingResponse pageTracking, Map<String, PageResponse> pages) {
        PageTrackingAdminResponse response = new PageTrackingAdminResponse();
        response.id = pageTracking.id;
        response.pageId = pageTracking.pageId;
        response.categoryId = pageTracking.categoryId;
        response.totalVisited = pageTracking.totalVisited;
        response.createdTime = pageTracking.createdTime;
        response.createdBy = pageTracking.createdBy;
        response.timestamp = pageTracking.timestamp;
        PageResponse pageResponse = pages.get(pageTracking.pageId);
        if (pageResponse != null) {
            response.title = pageResponse.title;
            response.path = pageResponse.path;
        } else {
            response.title = pageTracking.pageId;
        }
        return response;
    }

}
