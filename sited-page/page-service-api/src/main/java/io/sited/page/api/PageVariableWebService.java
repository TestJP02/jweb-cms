package io.sited.page.api;


import io.sited.page.api.variable.CreateVariableRequest;
import io.sited.page.api.variable.DeleteVariableRequest;
import io.sited.page.api.variable.UpdateVariableRequest;
import io.sited.page.api.variable.VariableQuery;
import io.sited.page.api.variable.VariableResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/page/variable")
public interface PageVariableWebService {
    @Path("/{id}")
    @GET
    VariableResponse get(@PathParam("id") String id);

    @Path("/find")
    @PUT
    QueryResponse<VariableResponse> find(VariableQuery query);

    @GET
    List<VariableResponse> find();

    @POST
    VariableResponse create(CreateVariableRequest request);

    @Path("/{id}")
    @PUT
    VariableResponse update(@PathParam("id") String id, UpdateVariableRequest request);

    @Path("/batch-delete")
    @PUT
    void delete(DeleteVariableRequest request);
}
