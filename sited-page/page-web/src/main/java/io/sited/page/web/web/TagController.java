package io.sited.page.web.web;

import com.google.common.collect.Maps;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.tag.PageTagResponse;
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
@Path("/tag")
public class TagController extends AbstractPageWebController {
    @Inject
    PageTagWebService pageTagWebService;

    @Path("/{tag}")
    @GET
    public Response tag(@PathParam("tag") String tag) {
        PageTagResponse pageTagResponse = pageTagWebService.findByName(tag).orElseThrow(() -> new NotFoundException("missing tag, tag=" + tag));
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("tag", pageTagResponse);
        return page(page(pageTagResponse), bindings);
    }

    private PageResponse page(PageTagResponse tag) {
        PageResponse page = new PageResponse();
        page.templatePath = "template/tag.html";
        page.title = tag.name;
        page.path = tag.path == null ? "/tag/" + tag.name : tag.path;
        page.createdTime = tag.createdTime;
        page.createdBy = tag.createdBy;
        page.updatedTime = tag.updatedTime;
        page.updatedBy = tag.updatedBy;
        return page;
    }
}
