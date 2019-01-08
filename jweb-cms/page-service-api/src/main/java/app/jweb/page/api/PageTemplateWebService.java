package app.jweb.page.api;


import app.jweb.page.api.template.PageTemplateResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author chi
 */
@Path("/api/page/template")
public interface PageTemplateWebService {
    @Path("/{pageId}")
    @GET
    PageTemplateResponse get(@PathParam("pageId") String pageId);
}
