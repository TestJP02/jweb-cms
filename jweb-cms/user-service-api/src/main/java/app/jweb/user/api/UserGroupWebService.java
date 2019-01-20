package app.jweb.user.api;


import app.jweb.user.api.group.DeleteUserGroupRequest;
import app.jweb.user.api.group.BatchGetRequest;
import app.jweb.user.api.group.CreateUserGroupRequest;
import app.jweb.user.api.group.UpdateUserGroupRequest;
import app.jweb.user.api.group.UserGroupQuery;
import app.jweb.user.api.group.UserGroupResponse;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/user/group")
public interface UserGroupWebService {
    @Path("/{id}")
    @GET
    UserGroupResponse get(@PathParam("id") String id);

    @Path("/name/{name}")
    @GET
    Optional<UserGroupResponse> findByName(@PathParam("name") String name);

    @Path("/batch-get")
    @PUT
    List<UserGroupResponse> batchGet(BatchGetRequest request);

    @PUT
    @Path("/find")
    QueryResponse<UserGroupResponse> find(UserGroupQuery query);

    @Path("/all")
    @GET
    List<UserGroupResponse> findAll();

    @POST
    UserGroupResponse create(CreateUserGroupRequest request);

    @Path("/{id}")
    @PUT
    UserGroupResponse update(@PathParam("id") String id, UpdateUserGroupRequest request);

    @Path("/batch-delete")
    @PUT
    void delete(DeleteUserGroupRequest request);

    @Path("/{id}/revert")
    @PUT
    void revert(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);
}
