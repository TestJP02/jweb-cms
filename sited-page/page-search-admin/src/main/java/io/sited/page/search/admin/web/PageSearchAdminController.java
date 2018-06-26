package io.sited.page.search.admin.web;


import io.sited.page.search.api.PageSearchWebService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/admin/api/page/search")
public class PageSearchAdminController {
    @Inject
    PageSearchWebService pageSearchWebService;

    @RolesAllowed("GET")
    @Path("/full-index")
    @PUT
    public Response fullIndex() {
        pageSearchWebService.fullIndex();
        return Response.ok().build();
    }
}
