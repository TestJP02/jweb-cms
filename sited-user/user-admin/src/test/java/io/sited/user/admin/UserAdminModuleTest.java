package io.sited.user.admin;

import io.sited.admin.AdminModule;
import io.sited.database.DatabaseModule;
import io.sited.email.smtp.SMTPModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.UserModuleImpl;
import io.sited.web.WebModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({UserAdminModule.class, SMTPModule.class, UserModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, WebModule.class, AdminModule.class})
class UserAdminModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }
}