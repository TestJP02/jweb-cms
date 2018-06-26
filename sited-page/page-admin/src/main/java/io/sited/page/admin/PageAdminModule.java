package io.sited.page.admin;


import com.google.common.collect.Lists;
import io.sited.admin.AbstractAdminModule;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleMenu;
import io.sited.page.admin.service.ChineseTagProvider;
import io.sited.page.admin.service.EnglishPathProvider;
import io.sited.page.admin.service.EnglishTagProvider;
import io.sited.page.admin.service.PageCategoryAccessService;
import io.sited.page.admin.service.PageTagManager;
import io.sited.page.admin.service.PathManager;
import io.sited.page.admin.web.api.PageAdminController;
import io.sited.page.admin.web.api.PageCategoryAdminController;
import io.sited.page.admin.web.api.PageCommentAdminController;
import io.sited.page.admin.web.api.PageComponentAdminController;
import io.sited.page.admin.web.api.PagePathAdminController;
import io.sited.page.admin.web.api.PageSavedComponentAdminController;
import io.sited.page.admin.web.api.PageTagAdminController;
import io.sited.page.admin.web.api.PageTemplateAdminController;
import io.sited.page.admin.web.api.PageVariableAdminController;

/**
 * @author chi
 */
public class PageAdminModule extends AbstractAdminModule implements PageAdminConfig {
    @Override
    protected void configure() {
        PageAdminOptions pageAdminOptions = options("page-admin", PageAdminOptions.class);

        PathManager pathManager = new PathManager();
        pathManager.setProvider("en", new EnglishPathProvider());

        PageTagManager pageTagManager = new PageTagManager();
        pageTagManager.setProvider("zh", new ChineseTagProvider());
        pageTagManager.setProvider("en", new EnglishTagProvider());

        bind(PageCategoryAccessService.class);

        bind(PathManager.class).toInstance(pathManager);
        bind(PageTagManager.class).toInstance(pageTagManager);
        bind(PageAdminOptions.class).toInstance(pageAdminOptions);

        admin().controller(PageAdminController.class);
        admin().controller(PagePathAdminController.class);
        admin().controller(PageCategoryAdminController.class);
        admin().controller(PageVariableAdminController.class);
        admin().controller(PageTemplateAdminController.class);
        admin().controller(PageSavedComponentAdminController.class);
        admin().controller(PageComponentAdminController.class);
        admin().controller(PageTagAdminController.class);
        admin().controller(PageCommentAdminController.class);

        admin().install(scriptModule());
    }


    @Override
    public PageAdminConfig setPathProvider(String language, PathProvider suggester) {
        require(PathManager.class).setProvider(language, suggester);
        return this;
    }

    @Override
    public PageAdminConfig setTagProvider(String language, TagProvider provider) {
        return null;
    }

    private ConsoleBundle scriptModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "page.menu";
        menu.path = "/admin/page/";

        ConsoleMenu.ConsoleMenuItem pageItem = new ConsoleMenu.ConsoleMenuItem();
        pageItem.displayName = "page.page";
        pageItem.path = "/admin/page/list";
        pageItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem componentItem = new ConsoleMenu.ConsoleMenuItem();
        componentItem.displayName = "page.components";
        componentItem.path = "/admin/page/component/list";
        componentItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem variableItem = new ConsoleMenu.ConsoleMenuItem();
        variableItem.displayName = "page.variable";
        variableItem.path = "/admin/page/variable/list";
        variableItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem categoryItem = new ConsoleMenu.ConsoleMenuItem();
        categoryItem.displayName = "page.category";
        categoryItem.path = "/admin/page/category/list";
        categoryItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem layoutItem = new ConsoleMenu.ConsoleMenuItem();
        layoutItem.displayName = "page.templates";
        layoutItem.path = "/admin/page/template/list";
        layoutItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem tagItem = new ConsoleMenu.ConsoleMenuItem();
        tagItem.displayName = "page.tags";
        tagItem.path = "/admin/page/tag/list";
        tagItem.rolesAllowed = Lists.newArrayList("page.LIST");

        menu.children = Lists.newArrayList(pageItem, categoryItem, tagItem, componentItem, variableItem, layoutItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "pageBundle";
        consoleModule.path = "/admin/page/";
        consoleModule.scriptFile = "admin/static/page/page.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/page-admin");
        consoleModule.menu = menu;
        return consoleModule;
    }
}
