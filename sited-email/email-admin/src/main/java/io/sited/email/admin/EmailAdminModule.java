package io.sited.email.admin;

import com.google.common.collect.Lists;
import io.sited.admin.AbstractAdminModule;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleMenu;
import io.sited.email.admin.web.EmailAdminController;
import io.sited.email.admin.web.EmailTemplateAdminController;

/**
 * @author chi
 */
public class EmailAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin()
            .controller(EmailAdminController.class)
            .controller(EmailTemplateAdminController.class);

        admin().install(jsModule());
    }

    private ConsoleBundle jsModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "email.menu";
        menu.path = "/admin/email/";
        menu.rolesAllowed = Lists.newArrayList("email.LIST");

        ConsoleMenu.ConsoleMenuItem userItem = new ConsoleMenu.ConsoleMenuItem();
        userItem.displayName = "email.email";
        userItem.path = "/admin/email/list";
        userItem.rolesAllowed = Lists.newArrayList("email.LIST");

        ConsoleMenu.ConsoleMenuItem roleItem = new ConsoleMenu.ConsoleMenuItem();
        roleItem.displayName = "email.template";
        roleItem.path = "/admin/email/template/list";
        roleItem.rolesAllowed = Lists.newArrayList("email.LIST");

        menu.children = Lists.newArrayList(userItem, roleItem);

        ConsoleBundle bundle = new ConsoleBundle();
        bundle.name = "emailBundle";
        bundle.path = "/admin/email/";
        bundle.scriptFile = "admin/static/email/email.min.js";
        bundle.messages = Lists.newArrayList("conf/messages/email-admin");
        bundle.menu = menu;

        return bundle;
    }
}
