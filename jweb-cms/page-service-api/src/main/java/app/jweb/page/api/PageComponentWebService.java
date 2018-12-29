package app.jweb.page.api;


import app.jweb.page.api.component.ComponentResponse;
import app.jweb.page.api.component.CreateComponentRequest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/page/component")
public interface PageComponentWebService {
    @GET
    List<ComponentResponse> find();

    @POST
    List<ComponentResponse> batchCreate(List<CreateComponentRequest> request);
}
