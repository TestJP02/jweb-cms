package io.sited.email.smtp.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.email.api.email.EmailQuery;
import io.sited.email.api.email.EmailResponse;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import io.sited.email.domain.EmailTracking;
import io.sited.email.service.EmailTrackingService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({SMTPModule.class, ServiceModule.class, DatabaseModule.class, MessageModule.class})
public class EmailWebServiceImplTest {
    @Inject
    MockApp app;
    @Inject
    EmailTrackingService emailTrackingService;
    EmailTracking emailTracking;

    @BeforeEach
    void setup() {
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.from = "test@sited.io";
        sendEmailRequest.to = Lists.newArrayList("test@sited.io");
        sendEmailRequest.replyTo = sendEmailRequest.from;
        sendEmailRequest.subject = "test";
        sendEmailRequest.content = "test";
        sendEmailRequest.requestBy = UUID.randomUUID().toString();
        SendEmailResponse sendEmailResponse = new SendEmailResponse();
        sendEmailResponse.id = UUID.randomUUID().toString();
        sendEmailResponse.status = SendEmailStatus.SUCCESS;
        sendEmailResponse.result = "success";
        emailTracking = emailTrackingService.success(sendEmailRequest, sendEmailResponse);
    }

    @Test
    void get() {
        ContainerResponse containerResponse = app.get("/api/email/" + emailTracking.id).execute();
        assertEquals(200, containerResponse.getStatus());
        EmailResponse emailResponse = (EmailResponse) containerResponse.getEntity();
        assertEquals(emailTracking.id, emailResponse.id);
        assertEquals(emailTracking.from, emailResponse.from);
    }

    @Test
    void find() {
        EmailQuery emailQuery = new EmailQuery();
        emailQuery.page = 1;
        emailQuery.limit = 10;
        ContainerResponse containerResponse = app.put("/api/email/find").setEntity(emailQuery).execute();
        assertEquals(200, containerResponse.getStatus());
        QueryResponse<EmailResponse> queryResponse = (QueryResponse) containerResponse.getEntity();
        assertEquals(1, queryResponse.total.intValue());
        assertEquals(emailTracking.id, queryResponse.items.get(0).id);
    }
}