package app.jweb.pincode;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PinCodeModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
public class PinCodeModuleImplTest {
    @Inject
    MockApp app;

    @Test
    public void configure() {
        assertNotNull(app);
    }
}