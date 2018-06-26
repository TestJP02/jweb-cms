package io.sited.email.admin.web;

import io.sited.email.admin.web.template.BatchDeleteEmailTemplateAdminRequest;
import io.sited.email.admin.web.template.CreateEmailTemplateAdminRequest;
import io.sited.email.admin.web.template.EmailTemplateAdminQuery;
import io.sited.email.admin.web.template.EmailTemplateAdminResponse;
import io.sited.email.admin.web.template.UpdateEmailTemplateAdminRequest;
import io.sited.email.api.EmailTemplateWebService;
import io.sited.email.api.template.BatchDeleteRequest;
import io.sited.email.api.template.CreateEmailTemplateRequest;
import io.sited.email.api.template.EmailTemplateQuery;
import io.sited.email.api.template.EmailTemplateResponse;
import io.sited.email.api.template.UpdateEmailTemplateRequest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/admin/api/email/template")
public class EmailTemplateAdminController {
    @Inject
    EmailTemplateWebService emailTemplateWebService;

    @RolesAllowed("CREATE")
    @POST
    public Response create(CreateEmailTemplateAdminRequest createEmailTemplateAdminRequest) throws Exception {
        return Response.ok(emailTemplateWebService.create(createRequest(createEmailTemplateAdminRequest))).build();
    }

    @RolesAllowed("GET")
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) throws Exception {
        return Response.ok(adminResponse(emailTemplateWebService.get(id))).build();
    }

    @RolesAllowed("LIST")
    @PUT
    @Path("/find")
    public Response find(EmailTemplateAdminQuery adminQuery) throws Exception {
        return Response.ok(emailTemplateWebService.find(query(adminQuery))).build();
    }

    @RolesAllowed("UPDATE")
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, UpdateEmailTemplateAdminRequest updateEmailTemplateAdminRequest) throws Exception {
        return Response.ok(emailTemplateWebService.update(id, updateRequest(updateEmailTemplateAdminRequest))).build();
    }

    @RolesAllowed("DELETE")
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) throws Exception {
        //todo requestBy
        emailTemplateWebService.delete(id, "admin");
        return Response.ok().build();
    }

    @RolesAllowed("DELETE")
    @POST
    @Path("/delete")
    public Response batchDelete(BatchDeleteEmailTemplateAdminRequest adminRequest) throws Exception {
        BatchDeleteRequest batchDeleteRequest = new BatchDeleteRequest();
        batchDeleteRequest.ids = adminRequest.ids;
        batchDeleteRequest.requestBy = "admin";
        emailTemplateWebService.batchDelete(batchDeleteRequest);
        return Response.ok().build();
    }

    private CreateEmailTemplateRequest createRequest(CreateEmailTemplateAdminRequest adminRequest) {
        CreateEmailTemplateRequest request = new CreateEmailTemplateRequest();
        request.name = adminRequest.name;
        request.subject = adminRequest.subject;
        request.content = adminRequest.content;
        request.requestBy = "admin";
        return request;
    }

    private EmailTemplateQuery query(EmailTemplateAdminQuery adminQuery) {
        EmailTemplateQuery query = new EmailTemplateQuery();
        query.query = adminQuery.query;
        query.status = adminQuery.status;
        query.sortingField = adminQuery.sortingField;
        query.desc = adminQuery.desc;
        query.page = adminQuery.page;
        query.limit = adminQuery.limit;
        return query;
    }

    private UpdateEmailTemplateRequest updateRequest(UpdateEmailTemplateAdminRequest adminRequest) {
        UpdateEmailTemplateRequest request = new UpdateEmailTemplateRequest();
        request.name = adminRequest.name;
        request.subject = adminRequest.subject;
        request.content = adminRequest.content;
        request.requestBy = "admin";
        return request;
    }

    private EmailTemplateAdminResponse adminResponse(EmailTemplateResponse response) {
        EmailTemplateAdminResponse adminResponse = new EmailTemplateAdminResponse();
        adminResponse.id = response.id;
        adminResponse.name = response.name;
        adminResponse.subject = response.subject;
        adminResponse.content = response.content;
        adminResponse.status = response.status;
        adminResponse.createdTime = response.createdTime;
        adminResponse.createdBy = response.createdBy;
        adminResponse.updatedTime = response.updatedTime;
        adminResponse.updatedBy = response.updatedBy;
        return adminResponse;
    }

}
