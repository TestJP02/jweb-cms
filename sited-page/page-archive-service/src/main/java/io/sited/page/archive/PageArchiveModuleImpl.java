package io.sited.page.archive;


import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.archive.api.PageArchiveModule;
import io.sited.page.archive.api.PageArchiveWebService;
import io.sited.page.archive.domain.PageArchive;
import io.sited.page.archive.service.message.PageChangedMessageHandler;
import io.sited.page.archive.service.message.PageDeletedMessageHandler;
import io.sited.page.archive.service.PageArchiveService;
import io.sited.page.archive.web.PageArchiveWebServiceImpl;

/**
 * @author chi
 */
public class PageArchiveModuleImpl extends PageArchiveModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageArchive.class);

        bind(PageArchiveService.class);

        module(MessageModule.class)
            .listen(PageChangedMessage.class, requestInjection(new PageChangedMessageHandler()))
            .listen(PageDeletedMessage.class, requestInjection(new PageDeletedMessageHandler()));

        api().service(PageArchiveWebService.class, PageArchiveWebServiceImpl.class);
    }
}
