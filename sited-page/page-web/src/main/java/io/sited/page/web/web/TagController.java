package io.sited.page.web.web;

import com.google.common.collect.ImmutableMap;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.web.AbstractPageController;
import io.sited.page.web.PageInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/tag")
public class TagController extends AbstractPageController {
    @Inject
    PageTagWebService pageTagWebService;

    @Path("/{tag}")
    @GET
    public Response tag(@PathParam("tag") String tag) {
        PageTagResponse pageTagResponse = pageTagWebService.findByName(tag).orElseThrow(() -> new NotFoundException("missing tag, tag=" + tag));
        return page(page(pageTagResponse), ImmutableMap.of());
    }

    private PageInfo page(PageTagResponse tag) {
        return PageInfo.builder()
            .setTitle(tag.name)
            .setPath(tag.path == null ? "/tag/" + tag.name : tag.path)
            .setTemplatePath("template/tag.html")
            .build();
    }
}
