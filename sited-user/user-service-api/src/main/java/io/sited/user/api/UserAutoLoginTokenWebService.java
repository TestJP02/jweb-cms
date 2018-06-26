package io.sited.user.api;


import io.sited.user.api.token.CreateUserAutoLoginTokenRequest;
import io.sited.user.api.token.DeleteUserAutoLoginTokenRequest;
import io.sited.user.api.token.UserAutoLoginTokenResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/user/token")
public interface UserAutoLoginTokenWebService {
    @Path("/user/{userId}")
    @GET
    Optional<UserAutoLoginTokenResponse> find(@PathParam("userId") String userId);

    @Path("/delete")
    @POST
    void delete(DeleteUserAutoLoginTokenRequest request);

    @POST
    UserAutoLoginTokenResponse create(CreateUserAutoLoginTokenRequest request);
}
