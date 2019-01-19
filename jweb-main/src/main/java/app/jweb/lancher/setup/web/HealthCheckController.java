package app.jweb.lancher.setup.web;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/health-check")
public class HealthCheckController {
    @GET
    public Response get() {
        return Response.ok().status(404).build();
    }
}
