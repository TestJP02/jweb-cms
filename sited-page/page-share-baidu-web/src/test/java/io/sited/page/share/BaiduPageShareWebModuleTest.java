package io.sited.page.share;

import io.sited.page.PageModuleImpl;
import io.sited.page.share.baidu.BaiduPageShareWebModule;
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
@Install({BaiduPageShareWebModule.class, WebModule.class, PageModuleImpl.class})
class BaiduPageShareWebModuleTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }
}