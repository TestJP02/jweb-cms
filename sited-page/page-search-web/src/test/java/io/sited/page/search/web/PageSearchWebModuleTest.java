package io.sited.page.search.web;


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
@Install({PageSearchWebModule.class, PageModule.class, WebModule.class, ServiceModule.class, PageSearchModule.class})
public class PageSearchWebModuleTest {
    @Inject
    MockApp app;

    @Test
    public void inject() {
        assertNotNull(app);
    }
}