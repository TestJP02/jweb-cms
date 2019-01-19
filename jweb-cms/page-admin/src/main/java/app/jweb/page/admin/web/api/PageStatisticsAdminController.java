package app.jweb.page.admin.web.api;

import app.jweb.page.admin.web.api.statistics.PageStatisticsAJAXResponse;
import app.jweb.page.api.PageStatisticsWebService;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.statistics.PageStatusStatisticsView;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

/**
 * @author miller
 */
@Path("/admin/api/page/statistics")
public class PageStatisticsAdminController {
    @Inject
    PageStatisticsWebService pageStatisticsWebService;
    @Inject
    PageWebService pageWebService;

    @Path("")
    @GET
    public Response statistics() {
        PageStatisticsAJAXResponse response = new PageStatisticsAJAXResponse();
        PageQuery pageQuery = new PageQuery();
        pageQuery.page = 1;
        pageQuery.limit = Integer.MAX_VALUE;
        response.total = pageWebService.find(pageQuery).total;
        response.pages = pageStatisticsWebService.statusStatistics().stream().map(this::ajaxResponse).collect(Collectors.toList());
        return Response.ok(response).build();
    }

    private PageStatisticsAJAXResponse.PageStatusStatistics ajaxResponse(PageStatusStatisticsView view) {
        PageStatisticsAJAXResponse.PageStatusStatistics statistics = new PageStatisticsAJAXResponse.PageStatusStatistics();
        statistics.status = view.status;
        statistics.total = view.total;
        return statistics;
    }
}
