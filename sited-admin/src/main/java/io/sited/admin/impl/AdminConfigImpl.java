package io.sited.admin.impl;

import io.sited.admin.AdminConfig;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleBundleConfig;
import io.sited.admin.impl.service.Console;
import io.sited.web.WebConfig;

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