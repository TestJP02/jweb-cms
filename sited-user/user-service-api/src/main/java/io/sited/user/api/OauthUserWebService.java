package io.sited.user.api;

import io.sited.user.api.oauth.CreateOauthUserRequest;
import io.sited.user.api.oauth.OauthLoginRequest;
import io.sited.user.api.oauth.OauthLoginResponse;
import io.sited.user.api.oauth.OauthUserResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/user/oauth")
public interface OauthUserWebService {
    @Path("/username/{username}")
    @GET
    @Deprecated
    Optional<OauthUserResponse> findByUsername(@PathParam("username") String username);

    @Path("/email/{email}")
    @GET
    @Deprecated
    Optional<OauthUserResponse> findByEmail(@PathParam("email") String email);

    @Path("/phone/{phone}")
    @GET
    @Deprecated
    Optional<OauthUserResponse> findByPhone(@PathParam("phone") String phone);

    @POST
    @Deprecated
    OauthUserResponse create(CreateOauthUserRequest request);

    @Path("/login")
    @POST
    @Deprecated
    OauthLoginResponse login(OauthLoginRequest request);
}
