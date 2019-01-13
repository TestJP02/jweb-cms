package app.jweb.page.web;

import app.jweb.ApplicationException;
import app.jweb.cache.CacheConfig;
import app.jweb.cache.CacheModule;
import app.jweb.cache.CacheOptions;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.page.api.PageComponentWebService;
import app.jweb.page.api.PageKeywordWebService;
import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.category.CategoryCreatedMessage;
import app.jweb.page.api.category.CategoryDeletedMessage;
import app.jweb.page.api.category.CategoryResponse;
import app.jweb.page.api.category.CategoryUpdatedMessage;
import app.jweb.page.api.component.CreateComponentRequest;
import app.jweb.page.api.component.SavedComponentChangedMessage;
import app.jweb.page.api.keyword.KeywordChangedMessage;
import app.jweb.page.api.page.PageCreatedMessage;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.variable.VariableChangedMessage;
import app.jweb.page.web.service.CategoryCacheService;
import app.jweb.page.web.service.HtmlComponentTemplateRepository;
import app.jweb.page.web.service.KeywordService;
import app.jweb.page.web.service.PageSitemap;
import app.jweb.page.web.service.PageTemplateRepository;
import app.jweb.page.web.service.VariableCacheService;
import app.jweb.page.web.service.component.AuthorComponent;
import app.jweb.page.web.service.component.BannerComponent;
import app.jweb.page.web.service.component.BreadcrumbComponent;
import app.jweb.page.web.service.component.FooterComponent;
import app.jweb.page.web.service.component.HeaderComponent;
import app.jweb.page.web.service.component.HeroComponent;
import app.jweb.page.web.service.component.ImageComponent;
import app.jweb.page.web.service.component.PostLinkListComponent;
import app.jweb.page.web.service.component.PostListComponent;
import app.jweb.page.web.service.component.PostPaginationComponent;
import app.jweb.page.web.service.component.SavedComponent;
import app.jweb.page.web.service.component.TextComponent;
import app.jweb.page.web.service.message.CategoryCreatedMessageHandler;
import app.jweb.page.web.service.message.CategoryDeletedMessageHandler;
import app.jweb.page.web.service.message.CategoryUpdatedMessageHandler;
import app.jweb.page.web.service.message.KeywordChangedMessageHandler;
import app.jweb.page.web.service.message.SavedComponentChangedMessageHandler;
import app.jweb.page.web.service.message.TemplateChangedMessageHandler;
import app.jweb.page.web.service.message.VariableChangedMessageHandler;
import app.jweb.page.web.web.PageController;
import app.jweb.template.Component;
import app.jweb.template.ComponentAttribute;
import app.jweb.template.TemplateEngine;
import app.jweb.web.AbstractWebModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PageWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        message("conf/messages/page-web");

        CacheConfig cacheConfig = module(CacheModule.class);
        cacheConfig.create(CategoryResponse.class, new CacheOptions());
        cacheConfig.create(VariableCacheService.VariableWrapper.class, new CacheOptions());
        cacheConfig.create(PageResponse.class, new CacheOptions());

        bind(VariableCacheService.class);
        bind(CategoryCacheService.class);
        bind(KeywordService.class).toInstance(new KeywordService());

        web().createCache("sitemap");
        web().addRepository(requestInjection(new HtmlComponentTemplateRepository()));
        web().addRepository(requestInjection(new PageTemplateRepository()));

        web().addComponent(requestInjection(new PostListComponent()));
        web().addComponent(requestInjection(new PostLinkListComponent()));
        web().addComponent(requestInjection(new PostLinkListComponent()));

        web().addComponent(requestInjection(new AuthorComponent()));
        web().addComponent(requestInjection(new BannerComponent()));
        web().addComponent(requestInjection(new BreadcrumbComponent()));
        web().addComponent(requestInjection(new FooterComponent()));
        web().addComponent(requestInjection(new HeaderComponent()));
        web().addComponent(requestInjection(new PostPaginationComponent()));
        web().addComponent(requestInjection(new TextComponent()));
        web().addComponent(requestInjection(new ImageComponent()));
        web().addComponent(requestInjection(new HeroComponent()));
//        web().addComponent(requestInjection(new CategoryPostListComponent()));
//        web().addComponent(requestInjection(new RecentPostListComponent()));
//        web().addComponent(requestInjection(new PostComponent()));
//        web().addComponent(requestInjection(new PostCardComponent()));
//        web().addComponent(requestInjection(new CategoryTreeComponent()));
//        web().addComponent(requestInjection(new TagPostListComponent()));
//        web().addComponent(requestInjection(new TrendingPostListComponent()));
//        web().addComponent(requestInjection(new PostNavigationComponent()));
//        web().addComponent(requestInjection(new RelatedPostListComponent()));
//        web().addComponent(requestInjection(new TagCloudComponent()));
//        web().addComponent(requestInjection(new PopularPostListComponent()));
//        web().addComponent(requestInjection(new UserPostLinkListComponent()));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(PageCreatedMessage.class, requestInjection(new TemplateChangedMessageHandler()));
        messageConfig.listen(VariableChangedMessage.class, requestInjection(new VariableChangedMessageHandler()));
        messageConfig.listen(SavedComponentChangedMessage.class, requestInjection(new SavedComponentChangedMessageHandler()));
        messageConfig.listen(CategoryCreatedMessage.class, requestInjection(new CategoryCreatedMessageHandler()));
        messageConfig.listen(CategoryUpdatedMessage.class, requestInjection(new CategoryUpdatedMessageHandler()));
        messageConfig.listen(CategoryDeletedMessage.class, requestInjection(new CategoryDeletedMessageHandler()));
        messageConfig.listen(KeywordChangedMessage.class, requestInjection(new KeywordChangedMessageHandler()));

        web().controller(PageController.class);

        web().addSiteMap(requestInjection(new PageSitemap()));

        onStartup(this::start);
    }

    public void start() {
        require(KeywordService.class)
            .setPageKeywordWebService(require(PageKeywordWebService.class))
            .start();
        initComponents();
    }

    private void initComponents() {
        TemplateEngine templateEngine = require(TemplateEngine.class);
        PageComponentWebService pageComponentWebService = require(PageComponentWebService.class);
        List<CreateComponentRequest> request = Lists.newArrayList();
        for (Component component : templateEngine.components()) {
            if (component instanceof AbstractPageComponent) {
                CreateComponentRequest createComponentRequest = new CreateComponentRequest();
                createComponentRequest.name = component.name();
                createComponentRequest.attributes = attributes(component.attributes());
                request.add(createComponentRequest);
            }
        }
        pageComponentWebService.batchCreate(request);

        PageSavedComponentWebService pageSavedComponentWebService = require(PageSavedComponentWebService.class);
        pageSavedComponentWebService.find().forEach(savedComponent -> {
            Component component = templateEngine.component(savedComponent.componentName).orElseThrow(() -> new ApplicationException("missing component, name={}", savedComponent.componentName));
            templateEngine.addComponent(new SavedComponent(component, savedComponent));
        });
    }

    private Map<String, Object> attributes(Map<String, ComponentAttribute<?>> attributes) {
        Map<String, Object> values = Maps.newHashMap();
        attributes.forEach((name, attribute) -> values.put(attribute.name(), attribute.defaultValue()));
        return values;
    }
}
