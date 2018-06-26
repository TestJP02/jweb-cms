package io.sited.page.web;


import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.template.TemplateQuery;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
public class PageTemplateWebServiceImplTest {
    @Inject
    MockApp app;

    @Test
    public void find() {
        ContainerResponse response = app.put("/api/page/template/find").setEntity(new TemplateQuery()).execute();
        assertEquals(200, response.getStatus());
    }
}