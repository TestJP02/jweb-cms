package io.sited.email.smtp.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.email.api.EmailTemplateWebService;
import io.sited.email.api.template.BatchDeleteRequest;
import io.sited.email.api.template.CreateEmailTemplateRequest;
import io.sited.email.api.template.EmailTemplateQuery;
import io.sited.email.api.template.EmailTemplateResponse;
import io.sited.email.api.template.EmailTemplateStatus;
import io.sited.email.api.template.UpdateEmailTemplateRequest;
import io.sited.email.smtp.SMTPModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.util.collection.QueryResponse;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({SMTPModule.class, ServiceModule.class, DatabaseModule.class, MessageModule.class})
public class EmailTemplateWebServiceImplTest {
    @Inject
    EmailTemplateWebService emailTemplateWebService;
    @Inject
    MockApp app;
    private String templateId;
    private EmailTemplateResponse emailTemplate;

    @BeforeEach
    public void setup() throws Exception {
        CreateEmailTemplateRequest request = new CreateEmailTemplateRequest();
        request.name = "test";
        request.subject = "test";
        request.content = "test";
        request.requestBy = "test";
        EmailTemplateResponse emailTemplate = emailTemplateWebService.create(request);
        this.templateId = emailTemplate.id;
        this.emailTemplate = emailTemplate;
    }

    @Test
    public void create() throws Exception {
        CreateEmailTemplateRequest request = new CreateEmailTemplateRequest();
        request.name = "new";
        request.subject = "new";
        request.content = "new";
        request.requestBy = "new";
        ContainerResponse response = app.post("/api/email/template").setEntity(request).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse emailTemplateResponse = (EmailTemplateResponse) response.getEntity();
        assertEquals(request.name, emailTemplateResponse.name);
        response = app.get("/api/email/template/" + emailTemplateResponse.id).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse emailTemplate = (EmailTemplateResponse) response.getEntity();
        assertEquals(request.name, emailTemplate.name);
        assertEquals(EmailTemplateStatus.ACTIVE, emailTemplateResponse.status);
    }

    @Test
    public void get() throws Exception {
        ContainerResponse response = app.get("/api/email/template/" + this.templateId).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse emailTemplateResponse = (EmailTemplateResponse) response.getEntity();
        assertEquals(this.templateId, emailTemplateResponse.id);
        assertEquals(this.emailTemplate.name, emailTemplateResponse.name);
    }

    @Test
    public void findByName() throws Exception {
        ContainerResponse response = app.get("/api/email/template/name/" + this.emailTemplate.name).execute();
        assertEquals(200, response.getStatus());
        Optional<EmailTemplateResponse> optional = (Optional<EmailTemplateResponse>) response.getEntity();
        assertTrue(optional.isPresent());
        EmailTemplateResponse emailTemplateResponse = optional.get();
        assertEquals(this.templateId, emailTemplateResponse.id);
        assertEquals(this.emailTemplate.subject, emailTemplateResponse.subject);
    }

    @Test
    public void find() throws Exception {
        EmailTemplateQuery query = new EmailTemplateQuery();
        query.query = this.emailTemplate.name;
        ContainerResponse response = app.put("/api/email/template/find").setEntity(query).execute();
        assertEquals(200, response.getStatus());
        QueryResponse<EmailTemplateResponse> emailTemplateResponses = (QueryResponse) response.getEntity();
        assertEquals(1, emailTemplateResponses.items.size());
        EmailTemplateResponse emailTemplateResponse = emailTemplateResponses.items.get(0);
        assertEquals(this.templateId, emailTemplateResponse.id);
    }

    @Test
    public void update() throws Exception {
        UpdateEmailTemplateRequest request = new UpdateEmailTemplateRequest();
        request.name = "update";
        request.subject = "update";
        request.content = "update";
        request.requestBy = "update";
        ContainerResponse response = app.put("/api/email/template/" + this.templateId).setEntity(request).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse emailTemplateResponse = (EmailTemplateResponse) response.getEntity();
        assertEquals(request.name, emailTemplateResponse.name);
        response = app.get("/api/email/template/" + this.templateId).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse check = (EmailTemplateResponse) response.getEntity();
        assertEquals(request.subject, check.subject);
    }

    @Test
    public void delete() throws Exception {
        BatchDeleteRequest batchDeleteRequest = new BatchDeleteRequest();
        batchDeleteRequest.ids = Lists.newArrayList(this.templateId);
        batchDeleteRequest.requestBy = "delete";
        ContainerResponse response = app.post("/api/email/template/delete-batch").setEntity(batchDeleteRequest).execute();
        assertEquals(204, response.getStatus());
        response = app.get("/api/email/template/" + this.templateId).execute();
        assertEquals(200, response.getStatus());
        EmailTemplateResponse emailTemplate = (EmailTemplateResponse) response.getEntity();
        assertEquals(EmailTemplateStatus.INACTIVE, emailTemplate.status);
        assertEquals("delete", emailTemplate.updatedBy);
        response = app.post("/api/email/template/delete-batch").setEntity(batchDeleteRequest).execute();
        assertEquals(204, response.getStatus());
    }

}