package io.sited.user.api;


import io.sited.user.api.user.ApplyPasswordRequest;
import io.sited.user.api.user.BatchDeleteUserRequest;
import io.sited.user.api.user.BatchGetUserRequest;
import io.sited.user.api.user.CreateUserRequest;
import io.sited.user.api.user.LoginRequest;
import io.sited.user.api.user.LoginResponse;
import io.sited.user.api.user.ResetPasswordRequest;
import io.sited.user.api.user.ResetPasswordResponse;
import io.sited.user.api.user.TokenLoginRequest;
import io.sited.user.api.user.UpdatePasswordRequest;
import io.sited.user.api.user.UpdateUserRequest;
import io.sited.user.api.user.UserQuery;
import io.sited.user.api.user.UserResponse;
import io.sited.user.api.user.UserView;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.DELETE;
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
@Path("/api/user")
public interface UserWebService {
    @Path("/login")
    @POST
    LoginResponse login(LoginRequest request);

    @Path("/login-token")
    @POST
    LoginResponse login(TokenLoginRequest request);

    @Path("/{id}")
    @GET
    UserResponse get(@PathParam("id") String id);

    @Path("/batch-get")
    @PUT
    List<UserView> batchGet(BatchGetUserRequest request);

    @PUT
    QueryResponse<UserResponse> find(UserQuery query);

    @Path("/username/{username}")
    @GET
    Optional<UserResponse> findByUsername(@PathParam("username") String username);

    @Path("/email/{email}")
    @GET
    Optional<UserResponse> findByEmail(@PathParam("email") String email);

    @Path("/phone/{phone}")
    @GET
    Optional<UserResponse> findByPhone(@PathParam("phone") String phone);

    @POST
    UserResponse create(CreateUserRequest request);

    @Path("/{id}")
    @PUT
    UserResponse update(@PathParam("id") String id, UpdateUserRequest request);

    @Path("/{id}/password")
    @PUT
    UserResponse updatePassword(@PathParam("id") String id, UpdatePasswordRequest request);

    @Path("/password/reset")
    @PUT
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);

    @Path("/password/reset/apply")
    @PUT
    void applyPassword(ApplyPasswordRequest request);

    @Path("/{id}/auto-login-token")
    @DELETE
    void deleteAutoLoginToken(@PathParam("id") String id);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteUserRequest request);

    @Path("/{id}/revert")
    @PUT
    void revert(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);
}
