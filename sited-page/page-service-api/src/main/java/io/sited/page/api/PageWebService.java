package io.sited.page.api;


import io.sited.page.api.page.CreatePageRequest;
import io.sited.page.api.page.DeletePageRequest;
import io.sited.page.api.page.LatestQuery;
import io.sited.page.api.page.PageNavigationResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.RevertDeletePageRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page")
public interface PageWebService {
    @Path("/{id}")
    @GET
    PageResponse get(@PathParam("id") String id);

    @Path("/batch-get")
    @PUT
    List<PageResponse> batchGet(List<String> ids);

    @Path("/path/{path}")
    @GET
    Optional<PageResponse> findByPath(@PathParam("path") String path);

    @Path("/find")
    @PUT
    QueryResponse<PageResponse> find(PageQuery query);

    @Path("/related")
    @PUT
    List<PageResponse> findRelated(PageRelatedQuery query);

    @Path("/{id}/navigation")
    @PUT
    PageNavigationResponse navigation(@PathParam("id") String id);

    @POST
    PageResponse create(CreatePageRequest request);

    @Path("/batch-delete")
    @POST
    void batchDelete(DeletePageRequest request);

    @Path("/revert")
    @POST
    void revertDelete(RevertDeletePageRequest request);

    @Path("/latest")
    @PUT
    List<PageResponse> latest(LatestQuery query);
}

