package app.jweb.post.admin.web.api;


import app.jweb.post.admin.service.PostPathService;
import app.jweb.post.admin.web.api.path.ValidatePathRequest;
import app.jweb.post.admin.web.api.path.ValidatePathResponse;
import app.jweb.post.admin.web.api.post.PostPathSuggestAJAXRequest;
import app.jweb.post.admin.web.api.post.PostPathSuggestAJAXResponse;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.draft.DraftResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.Optional;


/**
 * @author chi
 */
@Path("/admin/api/post/path")
public class PostPathAdminController {
    @Inject
    PostDraftWebService postDraftWebService;

    @Inject
    PostPathService postPathService;

    @RolesAllowed("GET")
    @PUT
    @Path("/suggest")
    public PostPathSuggestAJAXResponse suggestPath(PostPathSuggestAJAXRequest request) {
        PostPathSuggestAJAXResponse response = new PostPathSuggestAJAXResponse();
        response.path = postPathService.suggest(request.title);
        return response;
    }


    @RolesAllowed("LIST")
    @Path("/validate")
    @PUT
    public ValidatePathResponse validatePath(ValidatePathRequest request) {
        Optional<DraftResponse> drafts = postDraftWebService.findByPath(request.path);
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
