package app.jweb.page.admin.web.api;


import app.jweb.page.admin.service.PagePathService;
import app.jweb.page.admin.web.api.path.PagePathSuggestAJAXRequest;
import app.jweb.page.admin.web.api.path.PagePathSuggestAJAXResponse;
import app.jweb.page.admin.web.api.path.ValidatePathRequest;
import app.jweb.page.admin.web.api.path.ValidatePathResponse;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.Optional;


/**
 * @author chi
 */
@Path("/admin/api/page/path")
public class PagePathAdminController {
    @Inject
    PageWebService pageWebService;

    @Inject
    PagePathService pagePathService;

    @RolesAllowed("GET")
    @PUT
    @Path("/suggest")
    public PagePathSuggestAJAXResponse suggestPath(PagePathSuggestAJAXRequest request) {
        PagePathSuggestAJAXResponse response = new PagePathSuggestAJAXResponse();
        response.path = pagePathService.suggest(request.title);
        return response;
    }


    @RolesAllowed("LIST")
    @Path("/validate")
    @PUT
    public ValidatePathResponse validatePath(ValidatePathRequest request) {
        Optional<PageResponse> drafts = pageWebService.findByPath(request.path);
        ValidatePathResponse validatePathResponse = new ValidatePathResponse();
        validatePathResponse.valid = !drafts.isPresent() || drafts.get().id.equals(request.draftId);
        return validatePathResponse;
    }
}
