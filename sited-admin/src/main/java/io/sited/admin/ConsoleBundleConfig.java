package io.sited.admin;

import java.util.Map;

/**
 * @author chi
 */
public interface ConsoleBundleConfig {
    ConsoleBundleConfig addScriptFiles(String... scriptFiles);

    ConsoleBundleConfig addMessages(String... messages);

    ConsoleBundleConfig addOptions(Map<String, Object> options);

    ConsoleBundleConfig addMenuItems(ConsoleMenu.ConsoleMenuItem... items);

    ConsoleBundleConfig addRoute(String path, String bundleName);
}
