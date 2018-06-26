package io.sited.admin;

import io.sited.web.AbstractWebModule;

import java.util.List;

/**
 * @author chi
 */
public abstract class AbstractAdminModule extends AbstractWebModule {
    protected AbstractAdminModule() {
        super();
    }

    protected AbstractAdminModule(String name, List<String> dependencies) {
        super(name, dependencies);
    }

    protected AdminConfig admin() {
        return module(AdminModule.class);
    }
}
