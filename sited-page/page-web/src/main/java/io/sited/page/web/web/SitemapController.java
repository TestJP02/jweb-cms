package io.sited.page.web.web;

import io.sited.page.web.service.SitemapService;
import io.sited.resource.Resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/sitemap")
public class SitemapController {
    @Inject
    SitemapService sitemapService;

    @Inject
    UriInfo uriInfo;

    @Path("/sitemap.xml")
    @GET
    public Response index() {
        return Response.ok(sitemapService.index())
            .type("text/xml").build();
    }

    @Path("/{s:.+}")
    @GET
    public Response sitemap() {
        String path = uriInfo.getPath();
        Optional<Resource> resource = sitemapService.get(path);
        if (!resource.isPresent()) {
            throw new NotFoundException("missing sitemap, path=" + path);
        }
        return Response.ok(resource.get())
            .type("text/xml").build();
    }
}
