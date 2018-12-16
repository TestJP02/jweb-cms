package app.jweb.post;

import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.message.TopicOptions;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.PostContentWebService;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.PostKeywordWebService;
import app.jweb.post.api.PostModule;
import app.jweb.post.api.PostStatisticsWebService;
import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.PostVoteWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.category.CategoryCreatedMessage;
import app.jweb.post.api.category.CategoryDeletedMessage;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.category.CategoryUpdatedMessage;
import app.jweb.post.api.category.CreateCategoryRequest;
import app.jweb.post.api.keyword.KeywordChangedMessage;
import app.jweb.post.api.post.CommentCreatedMessage;
import app.jweb.post.api.post.CommentDeletedMessage;
import app.jweb.post.api.post.PostCreatedMessage;
import app.jweb.post.api.post.PostDeletedMessage;
import app.jweb.post.api.post.PostUpdatedMessage;
import app.jweb.post.api.post.PostVisitedMessage;
import app.jweb.post.domain.Post;
import app.jweb.post.domain.PostCategory;
import app.jweb.post.domain.PostContent;
import app.jweb.post.domain.PostDraft;
import app.jweb.post.domain.PostKeyword;
import app.jweb.post.domain.PostStatistics;
import app.jweb.post.domain.PostTag;
import app.jweb.post.domain.PostVoteTracking;
import app.jweb.post.service.PostCategoryService;
import app.jweb.post.service.PostContentService;
import app.jweb.post.service.PostDraftService;
import app.jweb.post.service.PostKeywordService;
import app.jweb.post.service.PostService;
import app.jweb.post.service.PostStatisticsService;
import app.jweb.post.service.PostTagService;
import app.jweb.post.service.PostVoteTrackingService;
import app.jweb.post.service.message.CommentCreatedMessageHandler;
import app.jweb.post.service.message.CommentDeletedMessageHandler;
import app.jweb.post.service.message.PostCreatedMessageHandler;
import app.jweb.post.service.message.PostDeletedMessageHandler;
import app.jweb.post.service.message.PostUpdatedMessageHandler;
import app.jweb.post.service.message.PostVisitedMessageHandler;
import app.jweb.post.service.task.ResetDailyVisitedTask;
import app.jweb.post.service.task.ResetMonthlyVisitedTask;
import app.jweb.post.service.task.ResetWeeklyVisitedTask;
import app.jweb.post.web.PostCategoryWebServiceImpl;
import app.jweb.post.web.PostContentWebServiceImpl;
import app.jweb.post.web.PostDraftWebServiceImpl;
import app.jweb.post.web.PostKeywordWebServiceImpl;
import app.jweb.post.web.PostStatisticsWebServiceImpl;
import app.jweb.post.web.PostTagWebServiceImpl;
import app.jweb.post.web.PostVoteWebServiceImpl;
import app.jweb.post.web.PostWebServiceImpl;
import app.jweb.scheduler.SchedulerConfig;
import app.jweb.scheduler.SchedulerModule;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PostModuleImpl extends PostModule {
    @Override
    protected void configure() {
        bind(PostOptions.class).toInstance(options("post", PostOptions.class));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(CategoryCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryUpdatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CategoryDeletedMessage.class, new TopicOptions());
        messageConfig.createTopic(KeywordChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(PostCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(PostUpdatedMessage.class, new TopicOptions());
        messageConfig.createTopic(PostDeletedMessage.class, new TopicOptions());
        messageConfig.createTopic(PostVisitedMessage.class, new TopicOptions());
        messageConfig.createTopic(CommentCreatedMessage.class, new TopicOptions());
        messageConfig.createTopic(CommentDeletedMessage.class, new TopicOptions());

        messageConfig.listen(PostCreatedMessage.class, requestInjection(new PostCreatedMessageHandler()));
        messageConfig.listen(PostUpdatedMessage.class, requestInjection(new PostUpdatedMessageHandler()));
        messageConfig.listen(PostDeletedMessage.class, requestInjection(new PostDeletedMessageHandler()));
        messageConfig.listen(PostVisitedMessage.class, requestInjection(new PostVisitedMessageHandler()));
        messageConfig.listen(PostVisitedMessage.class, requestInjection(new PostVisitedMessageHandler()));
        messageConfig.listen(CommentCreatedMessage.class, requestInjection(new CommentCreatedMessageHandler()));
        messageConfig.listen(CommentDeletedMessage.class, requestInjection(new CommentDeletedMessageHandler()));

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PostDraft.class)
            .entity(PostCategory.class)
            .entity(PostContent.class)
            .entity(Post.class)
            .entity(PostTag.class)
            .entity(PostKeyword.class)
            .entity(PostStatistics.class)
            .entity(PostVoteTracking.class);

        bind(PostKeywordService.class);
        bind(PostContentService.class);
        bind(PostCategoryService.class);
        bind(PostDraftService.class);
        bind(PostService.class);
        bind(PostTagService.class);
        bind(PostStatisticsService.class);
        bind(PostVoteTrackingService.class);

        SchedulerConfig schedulerConfig = module(SchedulerModule.class);
        schedulerConfig.schedule("0 0 0 * * ?", requestInjection(new ResetDailyVisitedTask()));
        schedulerConfig.schedule("0 0 0 ? * 1", requestInjection(new ResetWeeklyVisitedTask()));
        schedulerConfig.schedule("0 0 0 1 * ?", requestInjection(new ResetMonthlyVisitedTask()));

        api().service(PostCategoryWebService.class, PostCategoryWebServiceImpl.class);
        api().service(PostWebService.class, PostWebServiceImpl.class);
        api().service(PostDraftWebService.class, PostDraftWebServiceImpl.class);
        api().service(PostContentWebService.class, PostContentWebServiceImpl.class);
        api().service(PostKeywordWebService.class, PostKeywordWebServiceImpl.class);
        api().service(PostTagWebService.class, PostTagWebServiceImpl.class);
        api().service(PostStatisticsWebService.class, PostStatisticsWebServiceImpl.class);
        api().service(PostVoteWebService.class, PostVoteWebServiceImpl.class);

        onStartup(this::start);
    }

    private void start() {
        PostCategoryWebService pageCategoryService = require(PostCategoryWebService.class);
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
