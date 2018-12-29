package app.jweb.user.web;


import app.jweb.user.UserModuleImpl;
import app.jweb.user.api.oauth.OauthLoginRequest;
import app.jweb.user.api.oauth.OauthLoginResponse;
import app.jweb.user.api.oauth.Provider;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
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