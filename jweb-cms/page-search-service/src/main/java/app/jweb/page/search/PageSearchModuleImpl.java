package app.jweb.page.search;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.page.search.api.PageSearchModule;
import app.jweb.page.search.api.PageSearchWebService;
import app.jweb.page.search.api.SearchHistoryWebService;
import app.jweb.page.search.domain.SearchHistory;
import app.jweb.page.search.service.PageSearchService;
import app.jweb.page.search.service.SearchHistoryService;
import app.jweb.page.search.service.message.PageCreatedMessageHandler;
import app.jweb.page.search.service.message.PageDeletedMessageHandler;
import app.jweb.page.search.web.PageSearchWebServiceImpl;
import app.jweb.page.search.web.SearchHistoryWebServiceImpl;
import app.jweb.post.api.post.PostCreatedMessage;
import app.jweb.post.api.post.PostDeletedMessage;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class PageSearchModuleImpl extends PageSearchModule {
    public PageSearchModuleImpl() {
        super("app.jweb.page.search.api.impl", Lists.newArrayList("app.jweb.service", "app.jweb.message", "app.jweb.database", "app.jweb.page.search.api", "app.jwebk.page.api"));
    }

    @Override
    protected void configure() {
        module(DatabaseModule.class)
            .entity(SearchHistory.class);

        bind(PageSearchService.class);
        bind(SearchHistoryService.class);

        api().service(PageSearchWebService.class, PageSearchWebServiceImpl.class);
        api().service(SearchHistoryWebService.class, SearchHistoryWebServiceImpl.class);

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(PostCreatedMessage.class, requestInjection(new PageCreatedMessageHandler()));
        messageConfig.listen(PostDeletedMessage.class, requestInjection(new PageDeletedMessageHandler()));

        onStartup(this::start);
    }

    private void start() {
        require(PageSearchService.class).start();
    }
}
