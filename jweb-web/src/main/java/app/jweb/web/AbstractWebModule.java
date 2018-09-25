package app.jweb.web;

import app.jweb.AbstractModule;

import java.util.List;

/**
 * @author chi
 */
public abstract class AbstractWebModule extends AbstractModule {
    protected AbstractWebModule() {
        super();
    }

    protected AbstractWebModule(String name, List<String> dependencies) {
        super(name, dependencies);
    }

    protected WebConfig web() {
        return module(WebModule.class);
    }
}
