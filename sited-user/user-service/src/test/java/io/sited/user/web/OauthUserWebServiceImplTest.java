package io.sited.user.web;


import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.UserModuleImpl;
import io.sited.user.api.oauth.OauthLoginRequest;
import io.sited.user.api.oauth.OauthLoginResponse;
import io.sited.user.api.oauth.Provider;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class})
public class OauthUserWebServiceImplTest {
    @Inject
    MockApp app;

    @Test
    public void login() {
        OauthLoginRequest request = new OauthLoginRequest();
        request.email = "some@some.com";
        request.username = "some@some.com";
        request.requestBy = "some@some.com";
        request.provider = Provider.GOOGLE;
        request.autoLogin = true;

        ContainerResponse response = app.post("/api/user/oauth/login").setEntity(request).execute();
        OauthLoginResponse oauthLoginResponse = (OauthLoginResponse) response.getEntity();
        assertNotNull(oauthLoginResponse.autoLoginToken);
    }
}