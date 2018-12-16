package app.jweb.user.admin;

import app.jweb.user.UserModuleImpl;
import app.jweb.admin.AdminModule;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import app.jweb.web.WebModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({UserAdminModule.class, UserModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, WebModule.class, AdminModule.class})
class UserAdminModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }
}