package app.jweb.admin.impl;

import app.jweb.admin.AdminConfig;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleBundleConfig;
import app.jweb.admin.impl.service.Console;
import app.jweb.web.WebConfig;

/**
 * @author chi
 */
public class AdminConfigImpl implements AdminConfig {
    private final WebConfig webConfig;
    private final Console console;

    public AdminConfigImpl(WebConfig webConfig, Console console) {
        this.webConfig = webConfig;
        this.console = console;
    }

    @Override
    public <T> AdminConfig controller(Class<T> controllerClass) {
        webConfig.controller(controllerClass);
        return this;
    }

    @Override
    public AdminConfig install(ConsoleBundle consoleBundle) {
        console.install(consoleBundle);
        return this;
    }

    @Override
    public ConsoleBundleConfig bundle(String name) {
        return new ConsoleBundleConfigImpl(console.bundle(name));
    }
}