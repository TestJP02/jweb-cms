package io.sited.page.archive.api;


import io.sited.page.archive.api.archive.PageArchiveQuery;
import io.sited.page.archive.api.archive.PageArchiveResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/archive")
public interface PageArchiveWebService {
    @Path("/find")
    @PUT
    QueryResponse<PageArchiveResponse> find(PageArchiveQuery pageArchiveQuery);

    @Path("/{name}")
    @GET
    Optional<PageArchiveResponse> findByName(@PathParam("name") String name);
}
