package io.sited.page.web;

import com.google.common.collect.Maps;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.component.CreateSavedComponentRequest;
import io.sited.page.api.component.SavedComponentQuery;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.scheduler.SchedulerModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.util.collection.QueryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class SavedPageComponentWebServiceImplTest {
    @Inject
    PageSavedComponentWebService pageComponentWebService;

    @BeforeEach
    public void setup() {
        pageComponentWebService.create(create("Image"));
        pageComponentWebService.create(create("Image2"));
        pageComponentWebService.create(create("Text"));
    }

    @Test
    public void find() {
        SavedComponentQuery query = new SavedComponentQuery();
        query.query = "Image";
        QueryResponse<SavedComponentResponse> queryResponse = pageComponentWebService.find(query);
        assertEquals(2, queryResponse.items.size());
    }

    private CreateSavedComponentRequest create(String name) {
        CreateSavedComponentRequest request = new CreateSavedComponentRequest();
        request.name = name;
        request.attributes = Maps.newHashMap();
        return request;
    }
}