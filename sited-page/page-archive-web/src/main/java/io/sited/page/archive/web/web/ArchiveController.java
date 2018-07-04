package io.sited.page.archive.web.web;

import com.google.common.collect.ImmutableMap;
import io.sited.page.web.AbstractPageWebController;
import io.sited.page.web.PageInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/archive")
public class ArchiveController extends AbstractPageWebController {
    @Path("/{name}")
    @GET
    public Response archive(@PathParam("name") String name) {
        return page(page(name), ImmutableMap.of());
    }

    private PageInfo page(String name) {
        return PageInfo.builder()
            .setTitle("Archive " + name)
            .setTemplatePath("template/archive.html")
            .setPath("/archive/" + name)
            .build();
    }
}
