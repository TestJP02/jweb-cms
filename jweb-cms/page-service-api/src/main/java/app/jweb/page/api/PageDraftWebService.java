package app.jweb.page.api;

import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.GetPageDraftRequest;
import app.jweb.page.api.page.PageDraftQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PublishPageRequest;
import app.jweb.page.api.page.UpdatePageRequest;
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
@Path("/api/page/draft")
public interface PageDraftWebService {
    @Path("/find")
    @PUT
    QueryResponse<PageResponse> find(PageDraftQuery query);

    @Path("/path/{path}")
    @GET
    Optional<PageResponse> findByPath(@PathParam("path") String path);

    @Path("/get")
    @PUT
    PageResponse get(GetPageDraftRequest request);

    @POST
    PageResponse create(CreatePageRequest request);

    @Path("/{id}")
    @PUT
    PageResponse update(@PathParam("id") String id, UpdatePageRequest request);

    @Path("/publish")
    @PUT
    PageResponse publish(PublishPageRequest request);
}
