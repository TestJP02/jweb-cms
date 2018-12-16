package app.jweb.captcha.web;

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
@Install({CaptchaModule.class, WebModule.class})
class CaptchaModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }
}