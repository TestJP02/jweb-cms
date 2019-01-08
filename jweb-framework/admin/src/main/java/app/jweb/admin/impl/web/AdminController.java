package app.jweb.admin.impl.web;

import app.jweb.App;
import app.jweb.admin.ConsoleMenu;
import app.jweb.admin.impl.service.Console;
import app.jweb.admin.impl.service.ConsoleMessageBundle;
import app.jweb.admin.impl.service.ConsoleMessageBundleBuilder;
import app.jweb.admin.impl.service.ConsoleScriptBuilder;
import app.jweb.util.JSON;
import app.jweb.util.i18n.MessageBundle;
import app.jweb.web.AbstractWebController;
import app.jweb.web.ClientInfo;
import app.jweb.web.UserInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin")
public class AdminController extends AbstractWebController {
    @Inject
    App app;
    @Inject
    Console console;
    @Inject
    UserInfo userInfo;
    @Inject
    ClientInfo clientInfo;

    @GET
    public Response index() {
        return page();
    }

    @Path("/{s:.+}")
    @GET
    public Response page() {
        HashMap<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", "/");
        bindings.put("script", script(clientInfo.language(), userInfo));
        return template("admin/index.html", bindings).build();
    }

    private String script(String locale, UserInfo userInfo) {
        ConsoleMessageBundle messageBundle = new ConsoleMessageBundle(console.bundle("adminBundle"), app.message());
        return "window.app=" + new ConsoleScriptBuilder(console).build()
            + ";window.app.messages=" + JSON.toJSON(new ConsoleMessageBundleBuilder(messageBundle, locale).build())
            + ";window.app.menus=" + JSON.toJSON(menuViews(console.menus(), app.message(), locale))
            + ";window.app.user=" + JSON.toJSON(userInfoView(userInfo))
            + ";window.app.user.hasRole = function(role) {for (var i = 0; i < window.app.user.roles.length; i++) {if (window.app.user.roles[i] === role || window.app.user.roles[i] === \"*\") {return true;}return false;}}";
    }

    private UserInfoView userInfoView(UserInfo userInfo) {
        UserInfoView userInfoView = new UserInfoView();
        userInfoView.id = userInfo.id();
        userInfoView.imageURL = userInfo.imageURL();
        userInfoView.roles = userInfo.roles();
        userInfoView.username = userInfo.username();
        userInfoView.nickname = userInfo.nickname();
        return userInfoView;
    }

    private List<ConsoleMenuView> menuViews(List<ConsoleMenu> consoleMenus, MessageBundle messageManager, String language) {
        List<ConsoleMenuView> consoleMenuViews = Lists.newArrayList();
        for (ConsoleMenu consoleMenu : consoleMenus) {
            ConsoleMenuView consoleMenuView = new ConsoleMenuView();
            consoleMenuView.path = consoleMenu.path;
            consoleMenuView.displayName = messageManager.get(consoleMenu.messageKey == null ? consoleMenu.displayName : consoleMenu.messageKey, language).orElse(consoleMenu.displayName);
            consoleMenuView.displayOrder = consoleMenu.displayOrder;
            consoleMenuView.rolesAllowed = consoleMenu.rolesAllowed;
            if (consoleMenu.children != null) {
                consoleMenuView.children = consoleMenu.children.stream().map(item -> menuItemView(item, messageManager, language)).collect(Collectors.toList());
            }
            consoleMenuViews.add(consoleMenuView);
        }
        return consoleMenuViews;
    }

    private ConsoleMenuView.ConsoleMenuItemView menuItemView(ConsoleMenu.ConsoleMenuItem item, MessageBundle messageManager, String language) {
        ConsoleMenuView.ConsoleMenuItemView itemView = new ConsoleMenuView.ConsoleMenuItemView();
        itemView.path = item.path;
        itemView.displayName = messageManager.get(item.messageKey == null ? item.displayName : item.messageKey, language).orElse(item.displayName);
        itemView.displayOrder = item.displayOrder;
        itemView.rolesAllowed = item.rolesAllowed;
        return itemView;
    }

    public static class UserInfoView {
        public String id;
        public String username;
        public String nickname;
        public List<String> roles;
        public String imageURL;
    }
}
