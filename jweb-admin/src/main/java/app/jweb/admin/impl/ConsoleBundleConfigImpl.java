package app.jweb.admin.impl;

import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleBundleConfig;
import app.jweb.admin.ConsoleMenu;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/**
 * @author chi
 */
public class ConsoleBundleConfigImpl implements ConsoleBundleConfig {
    private final ConsoleBundle consoleBundle;

    public ConsoleBundleConfigImpl(ConsoleBundle consoleBundle) {
        this.consoleBundle = consoleBundle;
    }

    @Override
    public ConsoleBundleConfig addScriptFiles(String... scriptFiles) {
        consoleBundle.scriptFiles.addAll(Arrays.asList(scriptFiles));
        return this;
    }

    @Override
    public ConsoleBundleConfig addMessages(String... messages) {
        consoleBundle.messages.addAll(Arrays.asList(messages));
        return this;
    }

    @Override
    public ConsoleBundleConfig addOptions(Map<String, Object> options) {
        consoleBundle.options.putAll(options);
        return this;
    }

    @Override
    public ConsoleBundleConfig addMenuItems(ConsoleMenu.ConsoleMenuItem... items) {
        consoleBundle.menu.children.addAll(Arrays.asList(items));
        consoleBundle.menu.children.sort(Comparator.comparingInt(o -> o.displayOrder));
        return this;
    }

    @Override
    public ConsoleBundleConfig addRoute(String path, String bundleName) {
        consoleBundle.routes.put(path, bundleName);
        return this;
    }
}
