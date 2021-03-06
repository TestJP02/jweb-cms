package app.jweb.page;

import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.message.TopicOptions;
import app.jweb.page.api.PageCategoryWebService;
import app.jweb.page.api.PageComponentWebService;
import app.jweb.page.api.PageDraftWebService;
import app.jweb.page.api.PageKeywordWebService;
import app.jweb.page.api.PageModule;
import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.PageStatisticsWebService;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.PageVariableWebService;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.category.CategoryCreatedMessage;
import app.jweb.page.api.category.CategoryDeletedMessage;
import app.jweb.page.api.category.CategoryQuery;
import app.jweb.page.api.category.CategoryResponse;
import app.jweb.page.api.category.CategoryUpdatedMessage;
import app.jweb.page.api.category.CreateCategoryRequest;
import app.jweb.page.api.component.SavedComponentChangedMessage;
import app.jweb.page.api.keyword.KeywordChangedMessage;
import app.jweb.page.api.page.PageCreatedMessage;
import app.jweb.page.api.page.PageDeletedMessage;
import app.jweb.page.api.page.PageUpdatedMessage;
import app.jweb.page.api.statistics.PageVisitedMessage;
import app.jweb.page.api.variable.VariableChangedMessage;
import app.jweb.page.domain.Page;
import app.jweb.page.domain.PageCategory;
import app.jweb.page.domain.PageDraft;
import app.jweb.page.domain.PageKeyword;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.page.domain.PageStatistics;
import app.jweb.page.domain.PageTemplate;
import app.jweb.page.domain.PageVariable;
import app.jweb.page.service.PageCategoryService;
import app.jweb.page.service.PageComponentService;
import app.jweb.page.service.PageDraftService;
import app.jweb.page.service.PageKeywordService;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.page.service.PageService;
import app.jweb.page.service.PageStatisticsService;
import app.jweb.page.service.PageTemplateService;
import app.jweb.page.service.PageVariableService;
import app.jweb.page.web.PageCategoryWebServiceImpl;
import app.jweb.page.web.PageComponentWebServiceImpl;
import app.jweb.page.web.PageDraftWebServiceImpl;
import app.jweb.page.web.PageKeywordWebServiceImpl;
import app.jweb.page.web.PageSavedComponentWebServiceImpl;
import app.jweb.page.web.PageStatisticsWebServiceImpl;
import app.jweb.page.web.PageTemplateWebServiceImpl;
import app.jweb.page.web.PageVariableWebServiceImpl;
import app.jweb.page.web.PageWebServiceImpl;
import app.jweb.util.collection.QueryResponse;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class PageModuleImpl extends PageModule {
    @Override
    protected void configure() {
        bind(PageOptions.class).toInstance(options("page", PageOptions.class));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(PageCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageUpdatedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageDeletedMessage.class, new TopicOptions());
        messageConfig.createTopic(VariableChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(SavedComponentChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(KeywordChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryUpdatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryDeletedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageVisitedMessage.class, new TopicOptions());

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageVariable.class)
            .entity(Page.class)
            .entity(PageDraft.class)
            .entity(PageTemplate.class)
            .entity(PageCategory.class)
            .entity(PageKeyword.class)
            .entity(PageSavedComponent.class)
            .entity(PageStatistics.class);

        bind(PageVariableService.class);
        bind(PageSavedComponentService.class);
        bind(PageComponentService.class);
        bind(PageService.class);
        bind(PageCategoryService.class);
        bind(PageKeywordService.class);
        bind(PageTemplateService.class);
        bind(PageDraftService.class);
        bind(PageStatisticsService.class);

        api().service(PageVariableWebService.class, PageVariableWebServiceImpl.class);
        api().service(PageWebService.class, PageWebServiceImpl.class);
        api().service(PageComponentWebService.class, PageComponentWebServiceImpl.class);
        api().service(PageSavedComponentWebService.class, PageSavedComponentWebServiceImpl.class);
        api().service(PageKeywordWebService.class, PageKeywordWebServiceImpl.class);
        api().service(PageCategoryWebService.class, PageCategoryWebServiceImpl.class);
        api().service(PageTemplateWebService.class, PageTemplateWebServiceImpl.class);
        api().service(PageDraftWebService.class, PageDraftWebServiceImpl.class);
        api().service(PageStatisticsWebService.class, PageStatisticsWebServiceImpl.class);

        onStartup(this::start);
    }

    private void start() {
        PageCategoryWebService pageCategoryService = require(PageCategoryWebService.class);
        QueryResponse<CategoryResponse> category = pageCategoryService.find(new CategoryQuery());
        if (category.items.isEmpty()) {
            CreateCategoryRequest request = new CreateCategoryRequest();
            request.description = "Root directory";
            request.displayName = "Home";
            request.requestBy = "SYS";
            pageCategoryService.create(request);
        }
    }

    @Override
    public List<String> declareRoles() {
        List<String> declareRoles = Lists.newArrayList(super.declareRoles());
        declareRoles.add("PUBLISH");
        return declareRoles;
    }
}
