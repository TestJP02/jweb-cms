package io.sited.file.web.web.ajax;

import io.sited.file.api.DirectoryWebService;
import io.sited.file.api.directory.CreateDirectoryRequest;
import io.sited.file.web.web.ajax.directory.CreateDirectoryAJAXRequest;
import io.sited.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


/**
 * @author chi
 */
@Path("/web/api/directory")
public class DirectoryAJAXController {
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

    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        directoryService.delete(id, userInfo.username());
    }
}
