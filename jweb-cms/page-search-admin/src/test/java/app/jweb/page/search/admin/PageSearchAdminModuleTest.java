package app.jweb.page.search.admin;

import app.jweb.page.api.PageModule;
import app.jweb.page.search.api.PageSearchModule;
import app.jweb.admin.AdminModule;
import app.jweb.page.admin.PageAdminModule;
import app.jweb.service.ServiceModule;
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
@Install({PageSearchAdminModule.class, PageAdminModule.class, PageModule.class, AdminModule.class, WebModule.class, ServiceModule.class, PageSearchModule.class})
class PageSearchAdminModuleTest {
    @Inject
    MockApp app;

    @Test
    public void inject() {
        assertNotNull(app);
    }
}