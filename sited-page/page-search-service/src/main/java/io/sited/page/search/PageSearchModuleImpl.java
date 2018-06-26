package io.sited.page.search;

import com.google.common.collect.Lists;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.api.page.PagePublishedMessage;
import io.sited.page.search.api.PageSearchModule;
import io.sited.page.search.api.PageSearchWebService;
import io.sited.page.search.message.PageDeletedMessageHandler;
import io.sited.page.search.message.PagePublishedMessageHandler;
import io.sited.page.search.service.PageSearchService;
import io.sited.page.search.web.PageSearchWebServiceImpl;

/**
 * @author chi
 */
public class PageSearchModuleImpl extends PageSearchModule {
    public PageSearchModuleImpl() {
        super("sited.page.search", Lists.newArrayList("sited.service", "sited.message", "sited.page.search.api", "sited.page.api"));
    }

    @Override
    protected void configure() {
        bind(PageSearchService.class);

        api().service(PageSearchWebService.class, PageSearchWebServiceImpl.class);

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(PagePublishedMessage.class, PagePublishedMessageHandler.class);
        messageConfig.listen(PageDeletedMessage.class, PageDeletedMessageHandler.class);
    }

    @Override
    protected void onStartup() {
        require(PageSearchService.class).start();
    }
}
