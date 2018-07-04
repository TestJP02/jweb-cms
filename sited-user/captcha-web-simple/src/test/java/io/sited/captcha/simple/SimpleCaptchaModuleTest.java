package io.sited.captcha.simple;

import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.web.WebModule;
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