package io.sited.user.admin;


import com.google.common.collect.Lists;
import io.sited.admin.AbstractAdminModule;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleMenu;
import io.sited.user.admin.service.AdminForbiddenExceptionHandler;
import io.sited.user.admin.service.AdminNotAuthorizedExceptionHandler;
import io.sited.user.admin.web.ajax.UserAdminAJAXController;
import io.sited.user.admin.web.ajax.UserGroupAdminAJAXController;
import io.sited.user.admin.web.interceptor.RolesAllowedAdminInterceptor;
import io.sited.user.admin.web.interceptor.UserAdminInterceptor;

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