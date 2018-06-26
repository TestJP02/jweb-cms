package io.sited.page.search.admin;

import io.sited.admin.AdminModule;
import io.sited.page.admin.PageAdminModule;
import io.sited.page.api.PageModule;
import io.sited.page.search.api.PageSearchModule;
import io.sited.service.ServiceModule;
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
@Install({PageSearchAdminModule.class, PageAdminModule.class, PageModule.class, AdminModule.class, WebModule.class, ServiceModule.class, PageSearchModule.class})
class PageSearchAdminModuleTest {
    @Inject
    MockApp app;

    @Test
    public void inject() {
        assertNotNull(app);
    }
}