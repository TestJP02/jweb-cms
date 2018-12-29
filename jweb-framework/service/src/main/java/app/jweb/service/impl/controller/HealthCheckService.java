package app.jweb.service.impl.controller;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/api/health-check")
public interface HealthCheckService {
    @GET
    Response check();
}
