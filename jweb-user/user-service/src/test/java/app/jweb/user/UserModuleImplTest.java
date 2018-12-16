package app.jweb.user;


import app.jweb.user.api.UserWebService;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class})
public class UserModuleImplTest {
    @Inject
    UserWebService userWebService;

    @Test
    public void configure() {
        assertNotNull(userWebService);
    }
}