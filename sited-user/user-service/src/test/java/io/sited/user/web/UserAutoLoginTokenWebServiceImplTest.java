package io.sited.user.web;


import io.sited.database.DatabaseModule;
import io.sited.email.smtp.SMTPModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.UserModuleImpl;
import io.sited.user.api.UserAutoLoginTokenWebService;
import io.sited.user.api.token.CreateUserAutoLoginTokenRequest;
import io.sited.user.api.token.DeleteUserAutoLoginTokenRequest;
import io.sited.user.api.token.UserAutoLoginTokenResponse;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class, SMTPModule.class})
public class UserAutoLoginTokenWebServiceImplTest {
    @Inject
    MockApp app;
    @Inject
    UserAutoLoginTokenWebService userAutoLoginTokenWebService;
    UserAutoLoginTokenResponse token;

    @BeforeEach
    void setup() {
        CreateUserAutoLoginTokenRequest request = new CreateUserAutoLoginTokenRequest();
        request.userId = UUID.randomUUID().toString();
        request.expireTime = OffsetDateTime.now();
        request.requestBy = "test";

        token = userAutoLoginTokenWebService.create(request);
    }

    @Test
    void find() {
        ContainerResponse containerResponse = app.get("/api/user/token/user/" + token.userId).execute();
        assertEquals(200, containerResponse.getStatus());
        Optional<UserAutoLoginTokenResponse> optional = (Optional) containerResponse.getEntity();
        assertTrue(optional.isPresent());
        UserAutoLoginTokenResponse userAutoLoginTokenResponse = optional.get();
        assertEquals(token.id, userAutoLoginTokenResponse.id);
    }

    @Test
    void delete() {
        DeleteUserAutoLoginTokenRequest request = new DeleteUserAutoLoginTokenRequest();
        request.userId = token.userId;
        request.requestBy = UUID.randomUUID().toString();
        ContainerResponse containerResponse = app.post("/api/user/token/delete").setEntity(request).execute();
        assertEquals(204, containerResponse.getStatus());
    }

    @Test
    public void create() {
        CreateUserAutoLoginTokenRequest request = new CreateUserAutoLoginTokenRequest();
        request.userId = UUID.randomUUID().toString();
        request.expireTime = OffsetDateTime.now();
        request.requestBy = "test";

        ContainerResponse response = app.post("/api/user/token").setEntity(request).execute();
        UserAutoLoginTokenResponse token = (UserAutoLoginTokenResponse) response.getEntity();
        assertNotNull(token.token);
    }
}