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
import io.sited.page.api.page.PageChangedMessage;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateChangedMessage;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.web.component.ArchivePageListComponent;
import io.sited.page.web.component.AuthorComponent;
import io.sited.page.web.component.BannerComponent;
import io.sited.page.web.component.BreadcrumbComponent;
import io.sited.page.web.component.CategoryTreeComponent;
import io.sited.page.web.component.CommentListComponent;
import io.sited.page.web.component.ContentTableComponent;
import io.sited.page.web.component.FooterComponent;
import io.sited.page.web.component.HeaderComponent;
import io.sited.page.web.component.PageArchiveComponent;
import io.sited.page.web.component.PageCardComponent;
import io.sited.page.web.component.PageComponent;
import io.sited.page.web.component.PageLinkListComponent;
import io.sited.page.web.component.PageListComponent;
import io.sited.page.web.component.PageNavigationComponent;
import io.sited.page.web.component.PagePaginationComponent;
import io.sited.page.web.component.PageRelatedListComponent;
import io.sited.page.web.component.RecentPageListComponent;
import io.sited.page.web.component.SavedComponent;
import io.sited.page.web.component.TagCloudComponent;
import io.sited.page.web.component.TagPageListComponent;
import io.sited.page.web.message.CategoryChangedMessageHandler;
import io.sited.page.web.message.KeywordChangedMessageHandler;
import io.sited.page.web.message.PageChangedMessageHandler;
import io.sited.page.web.message.SavedComponentChangedMessageHandler;
import io.sited.page.web.message.TemplateChangedMessageHandler;
import io.sited.page.web.message.VariableChangedMessageHandler;
import io.sited.page.web.service.CachedCategoryService;
import io.sited.page.web.service.CachedPageContentService;
import io.sited.page.web.service.CachedPageService;
import io.sited.page.web.service.CachedTemplateService;
import io.sited.page.web.service.CachedVariableService;
import io.sited.page.web.service.HtmlComponentTemplateRepository;
import io.sited.page.web.service.KeywordManager;
import io.sited.page.web.service.PageTemplateRepository;
import io.sited.page.web.service.ThemeManager;
import io.sited.page.web.service.UserCommentNodeService;
import io.sited.page.web.web.ArchiveController;
import io.sited.page.web.web.IndexController;
import io.sited.page.web.web.PageController;
import io.sited.page.web.web.SitemapController;
import io.sited.page.web.web.TagController;
import io.sited.page.web.web.api.CommentWebController;
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
        cacheConfig.create(CachedVariableService.VariableWrapper.class, new CacheOptions());
        cacheConfig.create(PageContentResponse.class, new CacheOptions());
        cacheConfig.create(TemplateResponse.class, new CacheOptions());

        bind(CachedPageService.class);
        bind(CachedCategoryService.class);
        bind(CachedVariableService.class);
        bind(KeywordManager.class).toInstance(new KeywordManager());
        bind(CachedPageContentService.class);
        bind(CachedTemplateService.class);
        bind(ThemeManager.class);
        bind(UserCommentNodeService.class);

        web().addRepository(requestInjection(new HtmlComponentTemplateRepository()));
        web().addRepository(requestInjection(new PageTemplateRepository()));

        web().addComponent(requestInjection(new AuthorComponent()));
        web().addComponent(requestInjection(new BannerComponent()));
        web().addComponent(requestInjection(new BreadcrumbComponent()));
        web().addComponent(requestInjection(new PageListComponent()));
        web().addComponent(requestInjection(new PageLinkListComponent()));
        web().addComponent(requestInjection(new RecentPageListComponent()));
        web().addComponent(requestInjection(new PageComponent()));
        web().addComponent(requestInjection(new PageCardComponent()));
        web().addComponent(requestInjection(new ContentTableComponent()));
        web().addComponent(requestInjection(new FooterComponent()));
        web().addComponent(requestInjection(new HeaderComponent()));
        web().addComponent(requestInjection(new PageLinkListComponent()));
        web().addComponent(requestInjection(new PageNavigationComponent()));
        web().addComponent(requestInjection(new PageRelatedListComponent()));
        web().addComponent(requestInjection(new PagePaginationComponent()));
        web().addComponent(requestInjection(new CategoryTreeComponent()));
        web().addComponent(requestInjection(new TagCloudComponent()));
        web().addComponent(requestInjection(new PageArchiveComponent()));
        web().addComponent(requestInjection(new TagPageListComponent()));
        web().addComponent(requestInjection(new ArchivePageListComponent()));
        web().addComponent(requestInjection(new CommentListComponent()));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(TemplateChangedMessage.class, TemplateChangedMessageHandler.class);
        messageConfig.listen(VariableChangedMessage.class, VariableChangedMessageHandler.class);
        messageConfig.listen(SavedComponentChangedMessage.class, SavedComponentChangedMessageHandler.class);
        messageConfig.listen(CategoryChangedMessage.class, CategoryChangedMessageHandler.class);
        messageConfig.listen(KeywordChangedMessage.class, KeywordChangedMessageHandler.class);
        messageConfig.listen(PageChangedMessage.class, PageChangedMessageHandler.class);

        web().controller(PageController.class);
        web().controller(IndexController.class);
        web().controller(SitemapController.class);
        web().controller(TagController.class);
        web().controller(ArchiveController.class);
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
            CreateComponentRequest createComponentRequest = new CreateComponentRequest();
            createComponentRequest.name = component.name();
            createComponentRequest.attributes = attributes(component.attributes());
            request.add(createComponentRequest);
        }
        pageComponentWebService.batchCreate(request);

        PageSavedComponentWebService pageSavedComponentWebService = require(PageSavedComponentWebService.class);
        pageSavedComponentWebService.find().forEach(savedComponent -> {
            templateEngine.addComponent(new SavedComponent(savedComponent, templateEngine));
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

    private Map<String, Object> attributes(List<ComponentAttribute<?>> attributes) {
        Map<String, Object> values = Maps.newHashMap();
        attributes.forEach(attribute -> values.put(attribute.name(), attribute.defaultValue()));
        return values;
    }
}
