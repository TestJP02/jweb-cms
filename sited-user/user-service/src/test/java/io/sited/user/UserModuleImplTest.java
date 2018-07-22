package io.sited.user;


import io.sited.database.DatabaseModule;
import io.sited.email.smtp.SMTPModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.user.api.UserWebService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class, SMTPModule.class})
public class UserModuleImplTest {
    @Inject
    UserWebService userWebService;

    @Test
    public void configure() {
        assertNotNull(userWebService);
    }
}