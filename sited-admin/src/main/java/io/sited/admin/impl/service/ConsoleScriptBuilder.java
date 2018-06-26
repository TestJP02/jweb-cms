package io.sited.admin.impl.service;

import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleMenu;
import io.sited.admin.impl.web.ConsoleMenuView;
import io.sited.util.JSON;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class ConsoleScriptBuilder {
    private static final String TEMPLATE = "{"
        + "bundles: __bundles__,"
        + "bundle: function(bundleName) {"
        + "for (let i = 0; i < this.bundles.length; i++) {"
        + "if (this.bundles[i].name === bundleName) {"
        + "return this.bundles[i];"
        + "}"
        + "}"
        + "throw new Error(\"missing bundle, name=\" + bundleName);"
        + "}"
        + "}";
    private final Console console;

    public ConsoleScriptBuilder(Console console) {
        this.console = console;
    }

    public String build() {
        return TEMPLATE.replaceAll("__bundles__", JSON.toJSON(console.bundles().stream().map(this::view).collect(Collectors.toList())));
    }

    private ConsoleModuleView view(ConsoleBundle consoleBundle) {
        ConsoleModuleView view = new ConsoleModuleView();
        view.name = consoleBundle.name;
        view.options = consoleBundle.options;
        view.scriptFile = consoleBundle.scriptFile;
        view.path = consoleBundle.path;
        view.menu = menuView(consoleBundle.menu);
        return view;
    }

    private ConsoleMenuView menuView(ConsoleMenu consoleMenu) {
        if (consoleMenu == null) {
            return null;
        }
        ConsoleMenuView consoleMenuView = new ConsoleMenuView();
        consoleMenuView.path = consoleMenu.path;
        consoleMenuView.displayName = consoleMenu.displayName;
        consoleMenuView.displayOrder = consoleMenu.displayOrder;
        consoleMenuView.rolesAllowed = consoleMenu.rolesAllowed;
        if (consoleMenu.children != null) {
            consoleMenuView.children = consoleMenu.children.stream().map(this::menuItemView).collect(Collectors.toList());
        }
        return consoleMenuView;
    }

    private ConsoleMenuView.ConsoleMenuItemView menuItemView(ConsoleMenu.ConsoleMenuItem consoleMenu) {
        ConsoleMenuView.ConsoleMenuItemView consoleMenuView = new ConsoleMenuView.ConsoleMenuItemView();
        consoleMenuView.path = consoleMenu.path;
        consoleMenuView.displayName = consoleMenu.displayName;
        consoleMenuView.displayOrder = consoleMenu.displayOrder;
        consoleMenuView.rolesAllowed = consoleMenu.rolesAllowed;
        consoleMenuView.bundleName = consoleMenu.bundleName;
        return consoleMenuView;
    }

    public static class ConsoleModuleView {
        public String name;
        public String scriptFile;
        public String path;
        public Map<String, Object> options;
        public ConsoleMenuView menu;
    }
}
