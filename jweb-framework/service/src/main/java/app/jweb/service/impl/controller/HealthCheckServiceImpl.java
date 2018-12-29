package app.jweb.service.impl.controller;


import javax.ws.rs.core.Response;

/**
 * @author chi
 */
public class HealthCheckServiceImpl implements HealthCheckService {
    @Override
    public Response check() {
        return Response.ok().build();
    }
}
