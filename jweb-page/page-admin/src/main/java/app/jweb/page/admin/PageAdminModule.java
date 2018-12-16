package app.jweb.page.admin;


import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleMenu;
import app.jweb.page.admin.web.api.PageComponentAdminController;
import app.jweb.page.admin.web.api.PageSavedComponentAdminController;
import app.jweb.page.admin.web.api.PageTemplateAdminController;
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

        admin().controller(PageVariableAdminController.class);
        admin().controller(PageTemplateAdminController.class);
        admin().controller(PageSavedComponentAdminController.class);
        admin().controller(PageComponentAdminController.class);

        admin().install(scriptModule());
    }

    private ConsoleBundle scriptModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "page.menu";
        menu.path = "/admin/page/";

        ConsoleMenu.ConsoleMenuItem componentItem = new ConsoleMenu.ConsoleMenuItem();
        componentItem.displayName = "page.components";
        componentItem.path = "/admin/page/component/list";
        componentItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem variableItem = new ConsoleMenu.ConsoleMenuItem();
        variableItem.displayName = "page.variable";
        variableItem.path = "/admin/page/variable/list";
        variableItem.rolesAllowed = Lists.newArrayList("page.LIST");

        ConsoleMenu.ConsoleMenuItem layoutItem = new ConsoleMenu.ConsoleMenuItem();
        layoutItem.displayName = "page.templates";
        layoutItem.path = "/admin/page/template/list";
        layoutItem.rolesAllowed = Lists.newArrayList("page.LIST");

        menu.children = Lists.newArrayList(componentItem, variableItem, layoutItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "pageBundle";
        consoleModule.path = "/admin/page/";
        consoleModule.scriptFile = "admin/static/page/page.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/page-admin");
        consoleModule.menu = menu;
        return consoleModule;
    }
}
