package io.sited.page.api;


import io.sited.page.api.archive.PageArchiveQuery;
import io.sited.page.api.archive.PageArchiveResponse;
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

    @Path("/{year}/{month}")
    @GET
    Optional<PageArchiveResponse> findByYearAndMonth(@PathParam("year") Integer year, @PathParam("month") Integer month);
}
