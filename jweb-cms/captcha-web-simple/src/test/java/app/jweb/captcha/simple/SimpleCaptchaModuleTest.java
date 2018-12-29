package app.jweb.captcha.simple;

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
@Install({SimpleCaptchaModule.class, WebModule.class})
public class SimpleCaptchaModuleTest {
    @Inject
    MockApp app;

    @Test
    public void configure() {
        assertNotNull(app);
    }
}