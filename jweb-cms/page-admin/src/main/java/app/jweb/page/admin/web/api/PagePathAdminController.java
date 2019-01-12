package app.jweb.page.admin.web.api;


import app.jweb.page.admin.service.PagePathService;
import app.jweb.page.admin.web.api.path.PagePathSuggestAJAXRequest;
import app.jweb.page.admin.web.api.path.PagePathSuggestAJAXResponse;
import app.jweb.page.admin.web.api.path.ValidatePathAJAXRequest;
import app.jweb.page.admin.web.api.path.ValidatePathAJAXResponse;
import app.jweb.page.api.PageDraftWebService;
import app.jweb.page.api.page.ValidatePagePathRequest;
import app.jweb.page.api.page.ValidatePagePathResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;


/**
 * @author chi
 */
@Path("/admin/api/page/path")
public class PagePathAdminController {
    @Inject
    PageDraftWebService pageWebService;

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
    public ValidatePathAJAXResponse validatePath(ValidatePathAJAXRequest request) {
        ValidatePagePathRequest validatePagePathRequest = new ValidatePagePathRequest();
        validatePagePathRequest.draftId = request.draftId;
        validatePagePathRequest.path = request.path;
        ValidatePagePathResponse validatePathResponse = pageWebService.validatePath(validatePagePathRequest);
        ValidatePathAJAXResponse response = new ValidatePathAJAXResponse();
        response.valid = validatePathResponse.valid;
        return response;
    }
}
