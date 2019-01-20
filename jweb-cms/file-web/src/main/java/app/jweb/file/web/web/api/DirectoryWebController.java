package app.jweb.file.web.web.api;

import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.web.web.api.directory.CreateDirectoryAJAXRequest;
import app.jweb.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


/**
 * @author chi
 */
@Path("/web/api/directory")
public class DirectoryWebController {
    @Inject
    DirectoryWebService directoryService;
    @Inject
    UserInfo userInfo;

    @POST
    public void create(CreateDirectoryAJAXRequest createDirectoryAJAXRequest) {
        CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
        createDirectoryRequest.path = createDirectoryAJAXRequest.path;
        createDirectoryRequest.ownerId = createDirectoryAJAXRequest.ownerId;
        createDirectoryRequest.ownerRoles = createDirectoryAJAXRequest.ownerRoles;
        createDirectoryRequest.groupId = createDirectoryAJAXRequest.groupId;
        createDirectoryRequest.groupRoles = createDirectoryAJAXRequest.groupRoles;
        createDirectoryRequest.othersRoles = createDirectoryAJAXRequest.othersRoles;
        createDirectoryRequest.requestBy = userInfo.username();
        directoryService.create(createDirectoryRequest);
    }
}
