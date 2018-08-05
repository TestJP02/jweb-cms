package io.sited.page.api;


import io.sited.page.api.template.BatchCreateTemplateRequest;
import io.sited.page.api.template.BatchDeletePageRequest;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateQuery;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.template.UpdateTemplateRequest;
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
@Path("/api/page/template")
public interface PageTemplateWebService {
    @Path("/{id}")
    @GET
    TemplateResponse get(@PathParam("id") String id);

    @Path("/path/{path}")
    @GET
    Optional<TemplateResponse> findByTemplatePath(@PathParam("path") String path);

    @Path("/find")
    @PUT
    QueryResponse<TemplateResponse> find(TemplateQuery query);

    @POST
    TemplateResponse create(CreateTemplateRequest request);

    @Path("/batch-create")
    @POST
    void batchCreate(BatchCreateTemplateRequest request);

    @Path("/{id}")
    @PUT
    TemplateResponse update(@PathParam("id") String id, UpdateTemplateRequest request);

    @Path("/delete")
    @POST
    void batchDelete(BatchDeletePageRequest request);
}
