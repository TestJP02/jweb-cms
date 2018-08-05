package io.sited.page;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.message.TopicOptions;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageComponentWebService;
import io.sited.page.api.PageContentWebService;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.PageModule;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.PageStatisticsWebService;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.PageVariableWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryChangedMessage;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CreateCategoryRequest;
import io.sited.page.api.component.SavedComponentChangedMessage;
import io.sited.page.api.keyword.KeywordChangedMessage;
import io.sited.page.api.page.CommentCreatedMessage;
import io.sited.page.api.page.CommentDeletedMessage;
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.api.page.PageDeletedMessage;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.api.template.TemplateChangedMessage;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.domain.Page;
import io.sited.page.domain.PageCategory;
import io.sited.page.domain.PageContent;
import io.sited.page.domain.PageDraft;
import io.sited.page.domain.PageKeyword;
import io.sited.page.domain.PageSavedComponent;
import io.sited.page.domain.PageStatistics;
import io.sited.page.domain.PageTag;
import io.sited.page.domain.PageTemplate;
import io.sited.page.domain.PageVariable;
import io.sited.page.service.PageCategoryService;
import io.sited.page.service.PageComponentService;
import io.sited.page.service.PageContentService;
import io.sited.page.service.PageDraftService;
import io.sited.page.service.PageKeywordService;
import io.sited.page.service.PageLayoutService;
import io.sited.page.service.PageSavedComponentService;
import io.sited.page.service.PageService;
import io.sited.page.service.PageStatisticsService;
import io.sited.page.service.PageTagService;
import io.sited.page.service.PageTemplateService;
import io.sited.page.service.PageVariableService;
import io.sited.page.service.message.CommentCreatedMessageHandler;
import io.sited.page.service.message.CommentDeletedMessageHandler;
import io.sited.page.service.message.PageChangedMessageHandler;
import io.sited.page.service.message.PageDeletedMessageHandler;
import io.sited.page.service.message.PageVisitedMessageHandler;
import io.sited.page.service.task.ResetDailyVisitedTask;
import io.sited.page.service.task.ResetMonthlyVisitedTask;
import io.sited.page.service.task.ResetWeeklyVisitedTask;
import io.sited.page.web.PageCategoryWebServiceImpl;
import io.sited.page.web.PageComponentWebServiceImpl;
import io.sited.page.web.PageContentWebServiceImpl;
import io.sited.page.web.PageDraftWebServiceImpl;
import io.sited.page.web.PageKeywordWebServiceImpl;
import io.sited.page.web.PageSavedComponentWebServiceImpl;
import io.sited.page.web.PageStatisticsWebServiceImpl;
import io.sited.page.web.PageTagWebServiceImpl;
import io.sited.page.web.PageTemplateWebServiceImpl;
import io.sited.page.web.PageVariableWebServiceImpl;
import io.sited.page.web.PageWebServiceImpl;
import io.sited.scheduler.SchedulerConfig;
import io.sited.scheduler.SchedulerModule;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageModuleImpl extends PageModule {
    @Override
    protected void configure() {
        bind(PageOptions.class).toInstance(options("page", PageOptions.class));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(TemplateChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(VariableChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(SavedComponentChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(KeywordChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageDeletedMessage.class, new TopicOptions());
        messageConfig.createTopic(PageVisitedMessage.class, new TopicOptions());
        messageConfig.createTopic(CommentCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CommentDeletedMessage.class, new TopicOptions());

        messageConfig.listen(PageChangedMessage.class, requestInjection(new PageChangedMessageHandler()));
        messageConfig.listen(PageDeletedMessage.class, requestInjection(new PageDeletedMessageHandler()));
        messageConfig.listen(PageVisitedMessage.class, requestInjection(new PageVisitedMessageHandler()));
        messageConfig.listen(PageVisitedMessage.class, requestInjection(new PageVisitedMessageHandler()));
        messageConfig.listen(CommentCreatedMessage.class, requestInjection(new CommentCreatedMessageHandler()));
        messageConfig.listen(CommentDeletedMessage.class, requestInjection(new CommentDeletedMessageHandler()));

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageDraft.class)
            .entity(PageCategory.class)
            .entity(PageContent.class)
            .entity(Page.class)
            .entity(PageVariable.class)
            .entity(PageTemplate.class)
            .entity(PageSavedComponent.class)
            .entity(PageTag.class)
            .entity(PageKeyword.class)
            .entity(PageStatistics.class);

        bind(PageKeywordService.class);
        bind(PageContentService.class);
        bind(PageCategoryService.class);
        bind(PageDraftService.class);
        bind(PageService.class);
        bind(PageVariableService.class);
        bind(PageSavedComponentService.class);
        bind(PageComponentService.class);
        bind(PageTemplateService.class);
        bind(PageLayoutService.class);
        bind(PageTagService.class);
        bind(PageStatisticsService.class);

        SchedulerConfig schedulerConfig = module(SchedulerModule.class);
        schedulerConfig.schedule("0 0 0 * * ?", requestInjection(new ResetDailyVisitedTask()));
        schedulerConfig.schedule("0 0 0 ? * 1", requestInjection(new ResetWeeklyVisitedTask()));
        schedulerConfig.schedule("0 0 0 1 * ?", requestInjection(new ResetMonthlyVisitedTask()));

        api().service(PageCategoryWebService.class, PageCategoryWebServiceImpl.class);
        api().service(PageWebService.class, PageWebServiceImpl.class);
        api().service(PageDraftWebService.class, PageDraftWebServiceImpl.class);
        api().service(PageVariableWebService.class, PageVariableWebServiceImpl.class);
        api().service(PageTemplateWebService.class, PageTemplateWebServiceImpl.class);
        api().service(PageComponentWebService.class, PageComponentWebServiceImpl.class);
        api().service(PageSavedComponentWebService.class, PageSavedComponentWebServiceImpl.class);
        api().service(PageContentWebService.class, PageContentWebServiceImpl.class);
        api().service(PageKeywordWebService.class, PageKeywordWebServiceImpl.class);
        api().service(PageTagWebService.class, PageTagWebServiceImpl.class);
        api().service(PageStatisticsWebService.class, PageStatisticsWebServiceImpl.class);
    }

    @Override
    protected void onStartup() {
        PageCategoryWebService pageCategoryService = require(PageCategoryWebService.class);
        Optional<CategoryResponse> category = pageCategoryService.findByPath("/");
        if (!category.isPresent()) {
            CreateCategoryRequest request = new CreateCategoryRequest();
            request.path = "/";
            request.templatePath = "template/index.html";
            request.description = "/";
            request.displayName = "/";
            request.requestBy = "init";
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
