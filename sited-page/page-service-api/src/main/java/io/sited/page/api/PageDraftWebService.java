package io.sited.page.api;


import io.sited.page.api.draft.BatchDeleteDraftRequest;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftQuery;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.draft.UpdateDraftRequest;
import io.sited.page.api.page.PageResponse;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/draft")
public interface PageDraftWebService {
    @Path("/{id}")
    @GET
    DraftResponse get(@PathParam("id") String id);

    @Path("/find")
    @PUT
    QueryResponse<DraftResponse> find(DraftQuery query);

    @Path("/api/page/{pageId}/draft")
    @GET
    Optional<DraftResponse> findByPageId(@PathParam("pageId") String pageId);

    @Path("/path/{path}")
    @GET
    Optional<DraftResponse> findByPath(@PathParam("path") String path);

    @POST
    DraftResponse create(CreateDraftRequest request);

    @Path("/{id}")
    @PUT
    DraftResponse update(@PathParam("id") String id, UpdateDraftRequest request);

    @Path("/{id}/publish")
    @POST
    PageResponse publish(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteDraftRequest request);
}
