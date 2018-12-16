package app.jweb.pincode.web;

import app.jweb.database.DatabaseModule;
import app.jweb.pincode.PinCodeModuleImpl;
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
@Install({PinCodeWebModule.class, PinCodeModuleImpl.class, DatabaseModule.class})
public class PinCodeWebModuleTest {
    @Inject
    MockApp app;

    @Test
    public void configure() {
        assertNotNull(app);
    }
}