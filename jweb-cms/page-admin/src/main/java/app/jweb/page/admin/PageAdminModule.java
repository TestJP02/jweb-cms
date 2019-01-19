package app.jweb.page.admin;


import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleMenu;
import app.jweb.page.admin.service.PagePathService;
import app.jweb.page.admin.web.api.PageAdminController;
import app.jweb.page.admin.web.api.PageCategoryAdminController;
import app.jweb.page.admin.web.api.PageComponentAdminController;
import app.jweb.page.admin.web.api.PagePathAdminController;
import app.jweb.page.admin.web.api.PageSavedComponentAdminController;
import app.jweb.page.admin.web.api.PageStatisticsAdminController;
import app.jweb.page.admin.web.api.PageVariableAdminController;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class PageAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        PageAdminOptions pageAdminOptions = options("page-admin", PageAdminOptions.class);


        bind(PageAdminOptions.class).toInstance(pageAdminOptions);
        bind(PagePathService.class);

        admin().controller(PageVariableAdminController.class);
        admin().controller(PageAdminController.class);
        admin().controller(PageSavedComponentAdminController.class);
        admin().controller(PageComponentAdminController.class);
        admin().controller(PageCategoryAdminController.class);
        admin().controller(PagePathAdminController.class);
        admin().controller(PageStatisticsAdminController.class);

        admin().install(scriptModule());

        admin().bundle("dashboardBundle")
            .addMessages("conf/messages/page-dashboard")
            .addScriptFiles("admin/static/page/pageDashboard.min.js");

    }

    private ConsoleBundle scriptModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "page.menu";
        menu.path = "/admin/page/";

        ConsoleMenu.ConsoleMenuItem layoutItem = new ConsoleMenu.ConsoleMenuItem();
        layoutItem.displayName = "page.page";
        layoutItem.path = "/admin/page/list";
        layoutItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem categoryItem = new ConsoleMenu.ConsoleMenuItem();
        categoryItem.displayName = "page.category";
        categoryItem.path = "/admin/page/category/list";
        categoryItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem componentItem = new ConsoleMenu.ConsoleMenuItem();
        componentItem.displayName = "page.components";
        componentItem.path = "/admin/page/component/list";
        componentItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem variableItem = new ConsoleMenu.ConsoleMenuItem();
        variableItem.displayName = "page.variable";
        variableItem.path = "/admin/page/variable/list";
        variableItem.rolesAllowed = Lists.newArrayList("page.LIST");

        menu.children = Lists.newArrayList(layoutItem, categoryItem, componentItem, variableItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "pageBundle";
        consoleModule.path = "/admin/page/";
        consoleModule.scriptFile = "admin/static/page/page.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/page-admin");
        consoleModule.menu = menu;
        return consoleModule;
    }
}
