package io.sited.email.api;

import io.sited.email.api.template.BatchDeleteRequest;
import io.sited.email.api.template.CreateEmailTemplateRequest;
import io.sited.email.api.template.EmailTemplateQuery;
import io.sited.email.api.template.EmailTemplateResponse;
import io.sited.email.api.template.UpdateEmailTemplateRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.DELETE;
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
@Path("/api/email/template")
public interface EmailTemplateWebService {
    @POST
    EmailTemplateResponse create(CreateEmailTemplateRequest request);

    @GET
    @Path("/{id}")
    EmailTemplateResponse get(@PathParam("id") String id);

    @GET
    @Path("/name/{name}")
    Optional<EmailTemplateResponse> findByName(@PathParam("name") String name);

    @PUT
    @Path("/find/")
    QueryResponse<EmailTemplateResponse> find(EmailTemplateQuery query);

    @PUT
    @Path("/{id}")
    EmailTemplateResponse update(@PathParam("id") String id, UpdateEmailTemplateRequest request);

    @DELETE
    @Path("/{id}")
    @Deprecated
    void delete(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @POST
    @Path("/delete-batch/")
    void batchDelete(BatchDeleteRequest request);
}
