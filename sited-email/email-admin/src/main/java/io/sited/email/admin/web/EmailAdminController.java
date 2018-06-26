package io.sited.email.admin.web;

import io.sited.email.admin.web.email.EmailAdminQuery;
import io.sited.email.admin.web.email.EmailAdminResponse;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.EmailQuery;
import io.sited.email.api.email.EmailResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/admin/api/email")
public class EmailAdminController {
    @Inject
    EmailWebService emailWebService;

    @RolesAllowed("LIST")
    @PUT
    @Path("/find")
    public Response find(EmailAdminQuery emailAdminQuery) throws Exception {
        return Response.ok(emailWebService.find(emailQuery(emailAdminQuery))).build();
    }

    @RolesAllowed("GET")
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) throws Exception {
        return Response.ok(adminResponse(emailWebService.get(id))).build();
    }

    private EmailAdminResponse adminResponse(EmailResponse emailResponse) {
        EmailAdminResponse response = new EmailAdminResponse();
        response.id = emailResponse.id;
        response.from = emailResponse.from;
        response.replyTo = emailResponse.replyTo;
        response.to = emailResponse.to;
        response.subject = emailResponse.subject;
        response.content = emailResponse.content;
        response.status = emailResponse.status;
        response.errorMessage = emailResponse.errorMessage;
        response.createdTime = emailResponse.createdTime;
        response.createdBy = emailResponse.createdBy;
        return response;
    }

    private EmailQuery emailQuery(EmailAdminQuery emailAdminQuery) {
        EmailQuery emailQuery = new EmailQuery();
        emailQuery.query = emailAdminQuery.query;
        emailQuery.status = emailAdminQuery.status;
        emailQuery.sortingField = emailAdminQuery.sortingField;
        emailQuery.desc = emailAdminQuery.desc;
        emailQuery.page = emailAdminQuery.page;
        emailQuery.limit = emailAdminQuery.limit;
        return emailQuery;
    }
}
