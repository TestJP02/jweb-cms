package io.sited.page.web;


import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageContentWebService;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class})
public class PageContentWebServiceImplTest {
    @Inject
    PageDraftWebService pageDraftWebService;

    @Inject
    PageContentWebService pageContentWebService;

    PageResponse page;

    @BeforeEach
    public void setup() throws InterruptedException {
        DraftResponse page1 = pageDraftWebService.create(create("/1.html", "title 1", "description 1"));
        pageDraftWebService.publish(page1.id, "test");

        DraftResponse page2 = pageDraftWebService.create(create("/2.html", "title 2", "description 2"));
        page = pageDraftWebService.publish(page2.id, "test");

        pageDraftWebService.create(create("/3.html", "title 3", "description 3"));
    }

    @Test
    public void batchGet() {
        List<PageContentResponse> contents = pageContentWebService.batchGetByPageIds(Lists.newArrayList(page.id));
        assertEquals(1, contents.size());
    }

    private CreateDraftRequest create(String path, String title, String description) throws InterruptedException {
        Thread.sleep(1000);
        CreateDraftRequest request = new CreateDraftRequest();
        request.path = path;
        request.title = title;
        request.description = description;
        request.tags = Lists.newArrayList("tag");
        request.keywords = Lists.newArrayList("keyword");
        request.content = "test content";
        request.categoryId = "001";
        request.requestBy = "test";
        return request;
    }
}