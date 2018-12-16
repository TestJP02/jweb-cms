package app.jweb.page.api;


import app.jweb.page.api.template.BatchCreateTemplateRequest;
import app.jweb.page.api.template.BatchDeletePageRequest;
import app.jweb.page.api.template.CreateTemplateRequest;
import app.jweb.page.api.template.TemplateQuery;
import app.jweb.page.api.template.TemplateResponse;
import app.jweb.page.api.template.UpdateTemplateRequest;
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
@Path("/api/page/template")
public interface PageTemplateWebService {
    @Path("/{id}")
    @GET
    TemplateResponse get(@PathParam("id") String id);

    @Path("/template-path/{templatePath}")
    @GET
    Optional<TemplateResponse> findByTemplatePath(@PathParam("templatePath") String templatePath);

    @Path("/path/{path}")
    @GET
    Optional<TemplateResponse> findByPath(@PathParam("path") String path);

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
