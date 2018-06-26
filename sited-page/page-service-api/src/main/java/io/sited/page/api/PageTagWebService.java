package io.sited.page.api;


import io.sited.page.api.tag.BatchDeletePageTagRequest;
import io.sited.page.api.tag.CreatePageTagRequest;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.api.tag.UpdatePageTagRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/tag")
public interface PageTagWebService {
    @Path("/{id}")
    @GET
    PageTagResponse get(@PathParam("id") String id);

    @Path("/name/{name}")
    @GET
    Optional<PageTagResponse> findByName(@PathParam("name") String name);

    @Path("/find")
    @PUT
    QueryResponse<PageTagResponse> find(PageTagQuery query);

    @POST
    PageTagResponse create(CreatePageTagRequest request);

    @Path("/{id}")
    @PUT
    PageTagResponse update(@PathParam("id") String id, UpdatePageTagRequest request);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeletePageTagRequest request);
}
