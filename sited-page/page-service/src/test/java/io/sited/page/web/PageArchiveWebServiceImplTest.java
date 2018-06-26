package io.sited.page.web;


import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.archive.PageArchiveQuery;
import io.sited.page.api.archive.PageArchiveResponse;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.util.collection.QueryResponse;
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
public class PageArchiveWebServiceImplTest {
    @Inject
    MockApp mockApp;

    @Test
    public void find() {
        PageArchiveQuery pageArchiveQuery = new PageArchiveQuery();
        pageArchiveQuery.page = 1;
        pageArchiveQuery.limit = 10;
        ContainerResponse httpResponse = mockApp.put("/api/page/archive/find").setEntity(pageArchiveQuery).execute();
        assertEquals(200, httpResponse.getStatus());
        QueryResponse<PageArchiveResponse> queryResponse = (QueryResponse<PageArchiveResponse>) httpResponse.getEntity();
        assertEquals(0, queryResponse.total.longValue());
    }
}