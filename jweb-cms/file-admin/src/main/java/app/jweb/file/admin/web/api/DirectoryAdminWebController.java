package app.jweb.file.admin.web.api;

import app.jweb.file.admin.web.api.directory.CreateDirectoryAJAXRequest;
import app.jweb.file.admin.web.api.directory.DirectoryAJAXResponse;
import app.jweb.file.admin.web.api.directory.DirectoryNodeAJAXResponse;
import app.jweb.file.admin.web.api.directory.UpdateDirectoryAJAXRequest;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.api.directory.DeleteDirectoryRequest;
import app.jweb.file.api.directory.DirectoryNodeResponse;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.api.directory.UpdateDirectoryRequest;
import app.jweb.web.UserInfo;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/directory")
public class DirectoryAdminWebController {
    @Inject
    DirectoryWebService directoryService;
    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public DirectoryAJAXResponse get(@PathParam("id") String id) {
        return response(directoryService.get(id));
    }

    @RolesAllowed("CREATE")
    @POST
    public DirectoryAJAXResponse create(CreateDirectoryAJAXRequest createDirectoryAJAXRequest) {
        DirectoryResponse parentDirectory = directoryService.get(createDirectoryAJAXRequest.parentId);
        CreateDirectoryRequest instance = new CreateDirectoryRequest();
        instance.path = parentDirectory.path.substring(0, parentDirectory.path.length() - 1) + createDirectoryAJAXRequest.path;
        instance.description = createDirectoryAJAXRequest.description;
        instance.ownerId = createDirectoryAJAXRequest.ownerId;
        instance.ownerRoles = createDirectoryAJAXRequest.ownerRoles;
        instance.groupId = createDirectoryAJAXRequest.groupId;
        instance.groupRoles = createDirectoryAJAXRequest.groupRoles;
        instance.othersRoles = createDirectoryAJAXRequest.othersRoles;
        instance.requestBy = userInfo.username();
        return response(directoryService.create(instance));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public DirectoryAJAXResponse update(@PathParam("id") String id, UpdateDirectoryAJAXRequest updateDirectoryAJAXRequest) {
        UpdateDirectoryRequest instance = new UpdateDirectoryRequest();
        instance.description = updateDirectoryAJAXRequest.description;
        instance.ownerId = updateDirectoryAJAXRequest.ownerId;
        instance.ownerRoles = updateDirectoryAJAXRequest.ownerRoles;
        instance.groupId = updateDirectoryAJAXRequest.groupId;
        instance.groupRoles = updateDirectoryAJAXRequest.groupRoles;
        instance.othersRoles = updateDirectoryAJAXRequest.othersRoles;
        instance.requestBy = userInfo.username();
        return response(directoryService.update(id, instance));
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void delete(DeleteDirectoryRequest request) {
        directoryService.delete(request);
    }

    @RolesAllowed("LIST")
    @Path("/tree")
    @GET
    public List<DirectoryNodeAJAXResponse> tree() {
        return directoryService.tree().stream().map(this::response).collect(Collectors.toList());
    }

    @RolesAllowed("LIST")
    @Path("/first-two-levels")
    @GET
    public List<DirectoryNodeAJAXResponse> firstTwoLevels() {
        return directoryService.firstTwoLevels().stream().map(this::response).collect(Collectors.toList());
    }

    @RolesAllowed("LIST")
    @Path("/{id}/sub-tree")
    @GET
    public List<DirectoryNodeAJAXResponse> subTree(@PathParam("id") String id) {
        return directoryService.subTree(id).stream().map(this::response).collect(Collectors.toList());
    }


    private DirectoryNodeAJAXResponse response(DirectoryNodeResponse response) {
        DirectoryNodeAJAXResponse ajaxResponse = new DirectoryNodeAJAXResponse();
        ajaxResponse.id = response.directory.id;
        ajaxResponse.path = response.directory.path;
        ajaxResponse.name = response.directory.name;
        ajaxResponse.status = response.directory.status;
        ajaxResponse.ownerId = response.directory.ownerId;
        ajaxResponse.ownerRoles = response.directory.ownerRoles;
        ajaxResponse.groupId = response.directory.groupId;
        ajaxResponse.groupRoles = response.directory.groupRoles;
        ajaxResponse.othersRoles = response.directory.othersRoles;
        ajaxResponse.children = Lists.newArrayList();
        if (response.children != null) {
            for (DirectoryNodeResponse child : response.children) {
                ajaxResponse.children.add(response(child));
            }
        }
        ajaxResponse.createdTime = response.directory.createdTime;
        ajaxResponse.updatedTime = response.directory.updatedTime;
        return ajaxResponse;
    }


    private DirectoryAJAXResponse response(DirectoryResponse response) {
        DirectoryAJAXResponse ajaxResponse = new DirectoryAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.parentId = response.parentId;
        ajaxResponse.groupId = response.groupId;
        ajaxResponse.path = response.path;
        ajaxResponse.name = response.name;
        ajaxResponse.description = response.description;
        ajaxResponse.status = response.status;
        ajaxResponse.ownerId = response.ownerId;
        ajaxResponse.ownerRoles = response.ownerRoles;
        ajaxResponse.groupId = response.groupId;
        ajaxResponse.groupRoles = response.groupRoles;
        ajaxResponse.othersRoles = response.othersRoles;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedBy = response.updatedBy;
        return ajaxResponse;
    }
}
