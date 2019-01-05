package app.jweb.page.web.web;

import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.service.CategoryCacheService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/")
public class IndexController extends AbstractPageController {
    @Inject
    CategoryCacheService categoryService;

    @GET
    public Response get() {
        return Response.ok().build();
    }
}
