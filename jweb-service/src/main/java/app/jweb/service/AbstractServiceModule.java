package app.jweb.service;

import app.jweb.AbstractModule;

import java.util.List;

/**
 * @author chi
 */
public abstract class AbstractServiceModule extends AbstractModule {
    protected AbstractServiceModule() {
        super();
    }

    protected AbstractServiceModule(String name, List<String> dependencies) {
        super(name, dependencies);
    }

    protected ServiceConfig api() {
        return module(ServiceModule.class);
    }
}
