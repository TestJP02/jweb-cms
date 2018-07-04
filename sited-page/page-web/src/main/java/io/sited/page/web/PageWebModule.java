package io.sited.page.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.ApplicationException;
import io.sited.cache.CacheConfig;
import io.sited.cache.CacheModule;
import io.sited.cache.CacheOptions;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageComponentWebService;
import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.category.CategoryChangedMessage;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CreateCategoryRequest;
import io.sited.page.api.component.CreateComponentRequest;
import io.sited.page.api.component.SavedComponentChangedMessage;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.keyword.KeywordChangedMessage;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateChangedMessage;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.web.service.CategoryCacheService;
import io.sited.page.web.service.HtmlComponentTemplateRepository;
import io.sited.page.web.service.KeywordManager;
import io.sited.page.web.service.PageService;
import io.sited.page.web.service.PageTemplateRepository;
import io.sited.page.web.service.TemplateCacheService;
import io.sited.page.web.service.ThemeService;
import io.sited.page.web.service.UserCommentNodeService;
import io.sited.page.web.service.VariableCacheService;
import io.sited.page.web.service.component.AuthorComponent;
import io.sited.page.web.service.component.BannerComponent;
import io.sited.page.web.service.component.BreadcrumbComponent;
import io.sited.page.web.service.component.CategoryListComponent;
import io.sited.page.web.service.component.CategoryPageListComponent;
import io.sited.page.web.service.component.CategoryTreeComponent;
import io.sited.page.web.service.component.CommentListComponent;
import io.sited.page.web.service.component.ContentTableComponent;
import io.sited.page.web.service.component.FooterComponent;
import io.sited.page.web.service.component.HeaderComponent;
import io.sited.page.web.service.component.PageCardComponent;
import io.sited.page.web.service.component.PageComponent;
import io.sited.page.web.service.component.PageLinkListComponent;
import io.sited.page.web.service.component.PageListComponent;
import io.sited.page.web.service.component.PageNavigationComponent;
import io.sited.page.web.service.component.PagePaginationComponent;
import io.sited.page.web.service.component.RelatedPageListComponent;
import io.sited.page.web.service.component.PopularPageListComponent;
import io.sited.page.web.service.component.RecentPageListComponent;
import io.sited.page.web.service.component.SavedComponent;
import io.sited.page.web.service.component.TagCloudComponent;
import io.sited.page.web.service.component.TagPageListComponent;
import io.sited.page.web.service.component.ThemeCSSComponent;
import io.sited.page.web.service.component.ThemeScriptComponent;
import io.sited.page.web.service.message.CategoryChangedMessageHandler;
import io.sited.page.web.service.message.KeywordChangedMessageHandler;
import io.sited.page.web.service.message.SavedComponentChangedMessageHandler;
import io.sited.page.web.service.message.TemplateChangedMessageHandler;
import io.sited.page.web.service.message.VariableChangedMessageHandler;
import io.sited.page.web.service.processor.ThemeProcessor;
import io.sited.page.web.web.IndexController;
import io.sited.page.web.web.PageController;
import io.sited.page.web.web.SitemapController;
import io.sited.page.web.web.TagController;
import io.sited.page.web.web.api.CommentWebController;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.resource.FileResourceRepository;
import io.sited.template.Component;
import io.sited.template.ComponentAttribute;
import io.sited.template.TemplateEngine;
import io.sited.web.AbstractWebModule;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        PageWebOptions options = options("page-web", PageWebOptions.class);
        bind(PageWebOptions.class).toInstance(options);

        message("conf/messages/page-web");

        CacheConfig cacheConfig = module(CacheModule.class);
        cacheConfig.create(PageResponse.class, new CacheOptions());
        cacheConfig.create(CategoryResponse.class, new CacheOptions());
        cacheConfig.create(VariableCacheService.VariableWrapper.class, new CacheOptions());
        cacheConfig.create(PageContentResponse.class, new CacheOptions());
        cacheConfig.create(TemplateResponse.class, new CacheOptions());

        bind(PageService.class);
        bind(CategoryCacheService.class);
        bind(VariableCacheService.class);
        bind(KeywordManager.class).toInstance(new KeywordManager());
        bind(TemplateCacheService.class);
        bind(ThemeService.class);
        bind(UserCommentNodeService.class);

        web().addRepository(requestInjection(new HtmlComponentTemplateRepository()));
        web().addRepository(requestInjection(new PageTemplateRepository()));

        web().addComponent(requestInjection(new AuthorComponent()));
        web().addComponent(requestInjection(new BannerComponent()));
        web().addComponent(requestInjection(new BreadcrumbComponent()));
        web().addComponent(requestInjection(new CategoryPageListComponent()));
        web().addComponent(requestInjection(new PageLinkListComponent()));
        web().addComponent(requestInjection(new RecentPageListComponent()));
        web().addComponent(requestInjection(new PageComponent()));
        web().addComponent(requestInjection(new PageCardComponent()));
        web().addComponent(requestInjection(new ContentTableComponent()));
        web().addComponent(requestInjection(new FooterComponent()));
        web().addComponent(requestInjection(new HeaderComponent()));
        web().addComponent(requestInjection(new PageLinkListComponent()));
        web().addComponent(requestInjection(new PageNavigationComponent()));
        web().addComponent(requestInjection(new RelatedPageListComponent()));
        web().addComponent(requestInjection(new PagePaginationComponent()));
        web().addComponent(requestInjection(new CategoryTreeComponent()));
        web().addComponent(requestInjection(new TagCloudComponent()));
        web().addComponent(requestInjection(new TagPageListComponent()));
        web().addComponent(requestInjection(new CommentListComponent()));
        web().addComponent(requestInjection(new PopularPageListComponent()));
        web().addComponent(requestInjection(new CategoryListComponent()));
        web().addComponent(requestInjection(new PageListComponent()));
        web().addComponent(requestInjection(new ThemeCSSComponent()));
        web().addComponent(requestInjection(new ThemeScriptComponent()));

        if (options.theme != null) {
            web().addElementProcessor(new ThemeProcessor(options.theme));

            ClasspathResourceRepository classpathResourceRepository = new ClasspathResourceRepository("web/theme/" + options.theme);
            web().addRepository(classpathResourceRepository);

            FileResourceRepository fileResourceRepository = new FileResourceRepository(app().dir().resolve("web/theme/" + options.theme));
            web().addRepository(fileResourceRepository);
        }

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(TemplateChangedMessage.class, requestInjection(new TemplateChangedMessageHandler()));
        messageConfig.listen(VariableChangedMessage.class, requestInjection(new VariableChangedMessageHandler()));
        messageConfig.listen(SavedComponentChangedMessage.class, requestInjection(new SavedComponentChangedMessageHandler()));
        messageConfig.listen(CategoryChangedMessage.class, requestInjection(new CategoryChangedMessageHandler()));
        messageConfig.listen(KeywordChangedMessage.class, requestInjection(new KeywordChangedMessageHandler()));

        web().controller(PageController.class);
        web().controller(IndexController.class);
        web().controller(SitemapController.class);
        web().controller(TagController.class);

        web().controller(CommentWebController.class);
    }

    @Override
    public void onStartup() {
        require(KeywordManager.class)
            .setPageKeywordWebService(require(PageKeywordWebService.class))
            .start();
        initTemplates();
        initComponents();
        initCategories();
    }

    private void initTemplates() {
        Path web = app().dir().resolve("web");
        PageTemplateWebService pageTemplateWebService = require(PageTemplateWebService.class);
        try {
            Path template = web.resolve("template");
            if (!template.toFile().exists()) {
                return;
            }
            Files.walkFileTree(template, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String path = web.relativize(file).toString().replaceAll("\\\\", "/");
                    if (path.endsWith(".html")) {
                        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
                        createTemplateRequest.path = path;
                        createTemplateRequest.displayName = path + "(Read Only)";
                        createTemplateRequest.readOnly = true;
                        createTemplateRequest.requestBy = "init";
                        pageTemplateWebService.create(createTemplateRequest);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
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

    private void initCategories() {
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

    private Map<String, Object> attributes(Map<String, ComponentAttribute<?>> attributes) {
        Map<String, Object> values = Maps.newHashMap();
        attributes.forEach((name, attribute) -> values.put(attribute.name(), attribute.defaultValue()));
        return values;
    }
}
