package io.sited.service.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author chi
 */
@Path("/some")
public interface TestService {
    @Path("/some")
    @GET
    List<String> list();

    @Path("/some/{id}")
    @GET
    List<String> get(@PathParam("id") String id);
}
