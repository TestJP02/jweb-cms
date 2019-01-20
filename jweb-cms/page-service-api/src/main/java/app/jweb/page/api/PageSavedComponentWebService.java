package app.jweb.page.api;


import app.jweb.page.api.component.DeleteSavedComponentRequest;
import app.jweb.page.api.component.CreateSavedComponentRequest;
import app.jweb.page.api.component.SavedComponentQuery;
import app.jweb.page.api.component.SavedComponentResponse;
import app.jweb.page.api.component.UpdateSavedComponentRequest;
import app.jweb.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/saved-component")
public interface PageSavedComponentWebService {
    @Path("/find")
    @GET
    QueryResponse<SavedComponentResponse> find();

    @Path("/find")
    @PUT
    QueryResponse<SavedComponentResponse> find(SavedComponentQuery query);

    @Path("/{id}")
    @GET
    SavedComponentResponse get(@PathParam("id") String id);

    @Path("/id/{id}")
    @GET
    Optional<SavedComponentResponse> findById(@PathParam("id") String id);

    @Path("/name/{name}")
    @GET
    Optional<SavedComponentResponse> findByName(@PathParam("name") String name);

    @POST
    SavedComponentResponse create(CreateSavedComponentRequest request);

    @Path("/{id}")
    @PUT
    SavedComponentResponse update(@PathParam("id") String id, UpdateSavedComponentRequest request);

    @Path("/batch-delete")
    @POST
    void delete(DeleteSavedComponentRequest request);
}
