package io.sited.page.admin.web.api;


import io.sited.page.admin.PathProvider;
import io.sited.page.admin.service.PathManager;
import io.sited.page.admin.web.api.page.PagePathSuggestAJAXRequest;
import io.sited.page.admin.web.api.page.PagePathSuggestAJAXResponse;
import io.sited.page.admin.web.api.path.ValidatePathRequest;
import io.sited.page.admin.web.api.path.ValidatePathResponse;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.draft.DraftResponse;

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
    PageDraftWebService pageDraftWebService;

    @Inject
    PathManager pathManager;

    @RolesAllowed("GET")
    @PUT
    @Path("/suggest")
    public PagePathSuggestAJAXResponse suggestPath(PagePathSuggestAJAXRequest request) {
        PagePathSuggestAJAXResponse response = new PagePathSuggestAJAXResponse();
        Optional<PathProvider> provider = pathManager.provider(request.language);
        if (provider.isPresent()) {
            response.path = provider.get().get(request.title);
        } else {
            response.path = "";
        }
        return response;
    }


    @RolesAllowed("LIST")
    @Path("/validate")
    @PUT
    public ValidatePathResponse validatePath(ValidatePathRequest request) {
        Optional<DraftResponse> drafts = pageDraftWebService.findByPath(request.path);
        ValidatePathResponse validatePathResponse = new ValidatePathResponse();
        validatePathResponse.valid = !drafts.isPresent() || drafts.get().id.equals(request.draftId);
        return validatePathResponse;
    }

//    private PathSuggester.SuggestedPath suggest(List<DirectoryResponse> directories) {
//        StringBuilder b = new StringBuilder();
//        for (DirectoryResponse directory : directories) {
//            b.append('/').append(directory.name);
//        }
//        b.append('/');
//        PathSuggester.SuggestedPath suggestedPath = new PathSuggester.SuggestedPath();
//        suggestedPath.path = b.toString();
//        return suggestedPath;
//    }
}
