package app.jweb.user.admin;


import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleMenu;
import app.jweb.user.admin.service.AdminForbiddenExceptionHandler;
import app.jweb.user.admin.service.AdminNotAuthorizedExceptionHandler;
import app.jweb.user.admin.web.ajax.UserAdminAJAXController;
import app.jweb.user.admin.web.ajax.UserGroupAdminAJAXController;
import app.jweb.user.admin.web.interceptor.RolesAllowedAdminInterceptor;
import app.jweb.user.admin.web.interceptor.UserAdminInterceptor;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.DynamicFeature;

/**
 * @author chi
 */
public class UserAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        UserAdminOptions options = options("user-admin", UserAdminOptions.class);
        bind(UserAdminOptions.class).toInstance(options);

        web().bindExceptionMapper(requestInjection(new AdminForbiddenExceptionHandler()));
        web().bindExceptionMapper(requestInjection(new AdminNotAuthorizedExceptionHandler()));

        app().register((DynamicFeature) (resourceInfo, context) -> {
            if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
                context.register(RolesAllowedAdminInterceptor.class);
            }
        });

        web().bindRequestFilter(requestInjection(new UserAdminInterceptor()));
        admin().controller(UserGroupAdminAJAXController.class);
        admin().controller(UserAdminAJAXController.class);
        admin().install(jsModule());
        admin().bundle("dashboardBundle")
            .addMessages("conf/messages/user-dashboard")
            .addScriptFiles("admin/static/user/userDashboard.min.js");
    }

    private ConsoleBundle jsModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "user.menu";
        menu.displayOrder = 30;
        menu.path = "/admin/user/";
        menu.rolesAllowed = Lists.newArrayList("user.LIST");

        ConsoleMenu.ConsoleMenuItem userItem = new ConsoleMenu.ConsoleMenuItem();
        userItem.displayName = "user.userList";
        userItem.path = "/admin/user/list";
        userItem.rolesAllowed = Lists.newArrayList("user.LIST");

        ConsoleMenu.ConsoleMenuItem userGroupItem = new ConsoleMenu.ConsoleMenuItem();
        userGroupItem.displayName = "user.userGroupList";
        userGroupItem.path = "/admin/user/group/list";
        userGroupItem.rolesAllowed = Lists.newArrayList("user.group.LIST");

        ConsoleMenu.ConsoleMenuItem userProfileItem = new ConsoleMenu.ConsoleMenuItem();
        userProfileItem.displayName = "user.userProfile";
        userProfileItem.path = "/admin/user/profile";
        userProfileItem.rolesAllowed = Lists.newArrayList("user.LIST");

        menu.children = Lists.newArrayList(userItem, userGroupItem, userProfileItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "userBundle";
        consoleModule.path = "/admin/user/";
        consoleModule.scriptFile = "admin/static/user/user.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/user-admin");
        consoleModule.menu = menu;

        return consoleModule;
    }
}