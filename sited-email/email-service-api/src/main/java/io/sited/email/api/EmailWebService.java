package io.sited.email.api;

import io.sited.email.api.email.EmailQuery;
import io.sited.email.api.email.EmailResponse;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendTemplateEmailRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author chi
 */
@Path("/api/email")
public interface EmailWebService {
    @POST
    SendEmailResponse send(SendEmailRequest request);

    @Path("/template/{name}")
    @POST
    SendEmailResponse send(@PathParam("name") String name, SendTemplateEmailRequest request);

    @Path("/async")
    @POST
    void sendAsync(SendEmailRequest request);

    @Path("/template/{name}/async")
    @POST
    void sendAsync(@PathParam("name") String name, SendTemplateEmailRequest request);

    @Path("/{id}")
    @GET
    EmailResponse get(@PathParam("id") String id);

    @Path("/find")
    @PUT
    QueryResponse<EmailResponse> find(EmailQuery query);
}
