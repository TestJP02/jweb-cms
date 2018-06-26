package io.sited.page.web.web;

import com.google.common.collect.Maps;
import io.sited.page.api.PageArchiveWebService;
import io.sited.page.api.archive.PageArchiveResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.AbstractPageWebController;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author chi
 */
@Path("/archive")
public class ArchiveController extends AbstractPageWebController {
    @Inject
    PageArchiveWebService pageArchiveWebService;

    @Path("/:year/:month")
    @GET
    public Response archive(@PathParam("year") Integer year, @PathParam("month") Integer month) {
        PageArchiveResponse pageArchiveResponse = pageArchiveWebService.findByYearAndMonth(year, month).orElseThrow(() -> new NotFoundException(String.format("missing archive,year=%s,month=%s", year, month)));
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("archive", pageArchiveResponse);
        return page(page(pageArchiveResponse), bindings);
    }

    private PageResponse page(PageArchiveResponse archive) {
        PageResponse page = new PageResponse();
        page.templatePath = "template/archive.html";
        page.title = null;
        page.path = "/archive/" + archive.year + "/" + archive.month;
        page.createdTime = archive.createdTime;
        page.createdBy = archive.createdBy;
        page.updatedTime = archive.updatedTime;
        page.updatedBy = archive.updatedBy;
        return page;
    }
}
