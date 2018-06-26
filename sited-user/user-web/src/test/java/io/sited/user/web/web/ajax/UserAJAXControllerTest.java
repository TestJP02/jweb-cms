package io.sited.user.web.web.ajax;

import io.sited.captcha.web.CaptchaModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.CreateUserRequest;
import io.sited.user.api.user.UserStatus;
import io.sited.user.web.UserWebModule;
import io.sited.user.web.web.ajax.user.ResetPasswordAJAXRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({UserWebModule.class, CaptchaModule.class})
public class UserAJAXControllerTest {
    @Inject
    UserWebService userWebService;
    @Inject
    MockApp app;

    @Test
    public void resetPassword() throws Exception {
        createIfNoneExist("some@test.com");
        ResetPasswordAJAXRequest resetPasswordAJAXRequest = new ResetPasswordAJAXRequest();
        resetPasswordAJAXRequest.username = "some@test.com";
        resetPasswordAJAXRequest.captchaCode = "";

        ContainerResponse response = app.post("/web/api/user/reset-password").setHeader("Accept", "application/json").setEntity(resetPasswordAJAXRequest).execute();
        assertEquals(200, response.getStatus());
    }

    private void createIfNoneExist(String username) {
        CreateUserRequest request = new CreateUserRequest();
        request.username = username;
        request.password = "some";
        request.status = UserStatus.ACTIVE;
        request.userGroupIds = new ArrayList<>();
        userWebService.create(request);
    }
}