package app.jweb.user.api;


import app.jweb.user.api.user.ApplyPasswordRequest;
import app.jweb.user.api.user.BatchDeleteUserRequest;
import app.jweb.user.api.user.BatchGetUserRequest;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.LoginRequest;
import app.jweb.user.api.user.LoginResponse;
import app.jweb.user.api.user.ResetPasswordRequest;
import app.jweb.user.api.user.ResetPasswordResponse;
import app.jweb.user.api.user.TokenLoginRequest;
import app.jweb.user.api.user.UpdatePasswordRequest;
import app.jweb.user.api.user.UpdateUserRequest;
import app.jweb.user.api.user.UserQuery;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.api.user.UserView;
import app.jweb.util.collection.QueryResponse;

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
