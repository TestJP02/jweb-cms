package app.jweb.user.admin.web.ajax;


import app.jweb.user.admin.web.ajax.group.CreateUserGroupAJAXRequest;
import app.jweb.user.admin.web.ajax.group.DeleteUserGroupAJAXRequest;
import app.jweb.user.admin.web.ajax.group.UpdateUserGroupWebRequest;
import app.jweb.user.admin.web.ajax.group.UserGroupAJAXResponse;
import app.jweb.user.admin.web.ajax.group.UserGroupFindAJAXRequest;
import app.jweb.user.api.UserGroupWebService;
import app.jweb.user.api.group.CreateUserGroupRequest;
import app.jweb.user.api.group.DeleteUserGroupRequest;
import app.jweb.user.api.group.UpdateUserGroupRequest;
import app.jweb.user.api.group.UserGroupQuery;
import app.jweb.user.api.group.UserGroupResponse;
import app.jweb.user.api.user.UserGroupStatus;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.UserInfo;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/user/group")
public class UserGroupAdminAJAXController {
    @Inject
    UserGroupWebService userGroupWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public UserGroupAJAXResponse get(@PathParam("id") String id) {
        return response(userGroupWebService.get(id));
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<UserGroupAJAXResponse> find(UserGroupFindAJAXRequest request) throws IOException {
        return userGroupWebService.find(query(request)).map(this::response);
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @GET
    public List<UserGroupAJAXResponse> findAll() {
        return userGroupWebService.findAll().stream().map(this::response).collect(Collectors.toList());
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public UserGroupAJAXResponse update(@PathParam("id") String id, UpdateUserGroupWebRequest updateUserGroupWebRequest) throws IOException {
        return response(userGroupWebService.update(id, updateUserGroupRequest(updateUserGroupWebRequest, userInfo)));
    }

    @RolesAllowed("CREATE")
    @POST
    public UserGroupAJAXResponse create(CreateUserGroupAJAXRequest createUserGroupAJAXRequest) throws IOException {
        return response(userGroupWebService.create(createUserGroupRequest(createUserGroupAJAXRequest, userInfo)));
    }


    @RolesAllowed("UPDATE")
    @Path("/{id}/revert")
    @PUT
    public void revert(@PathParam("id") String id) throws IOException {
        userGroupWebService.revert(id, userInfo.username());
    }

    @RolesAllowed("DELETE")
    @POST
    @Path("/batch-delete")
    public void batchDelete(DeleteUserGroupAJAXRequest request) {
        DeleteUserGroupRequest deleteUserGroupRequest = new DeleteUserGroupRequest();
        deleteUserGroupRequest.ids = request.ids;
        deleteUserGroupRequest.requestBy = userInfo.username();
        userGroupWebService.delete(deleteUserGroupRequest);
    }

    private UserGroupAJAXResponse response(UserGroupResponse instance) {
        UserGroupAJAXResponse response = new UserGroupAJAXResponse();
        response.id = instance.id;
        response.name = instance.name;
        response.status = instance.status;
        response.description = instance.description;
        response.roles = instance.roles;
        response.createdTime = instance.createdTime;
        response.updatedTime = instance.updatedTime;
        response.createdBy = instance.createdBy;
        response.updatedBy = instance.updatedBy;
        return response;
    }

    private UserGroupQuery query(UserGroupFindAJAXRequest request) {
        UserGroupQuery instance = new UserGroupQuery();
        instance.query = request.query;
        instance.status = request.status;
        instance.page = request.page;
        instance.limit = request.limit;
        return instance;
    }

    private UpdateUserGroupRequest updateUserGroupRequest(UpdateUserGroupWebRequest request, UserInfo user) {
        UpdateUserGroupRequest instance = new UpdateUserGroupRequest();
        instance.description = request.description;
        instance.roles = request.roles;
        instance.requestBy = user.username();
        return instance;
    }

    private CreateUserGroupRequest createUserGroupRequest(CreateUserGroupAJAXRequest request, UserInfo user) {
        CreateUserGroupRequest instance = new CreateUserGroupRequest();
        instance.name = request.name;
        instance.status = UserGroupStatus.ACTIVE;
        instance.displayName = request.displayName;
        instance.description = request.description;
        instance.roles = request.roles;
        instance.requestBy = user.username();
        return instance;
    }
}
