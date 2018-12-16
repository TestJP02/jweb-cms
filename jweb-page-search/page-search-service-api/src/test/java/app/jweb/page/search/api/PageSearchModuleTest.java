package app.jweb.page.search.api;

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
@Install({ServiceModule.class, PageSearchModule.class})
class PageSearchModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }
}