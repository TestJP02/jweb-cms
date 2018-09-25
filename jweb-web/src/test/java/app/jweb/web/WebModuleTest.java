package app.jweb.web;


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
@Install(WebModule.class)
class WebModuleTest {
    @Inject
    MockApp app;

    @Test
    public void configure() {
        assertNotNull(app);
    }
}