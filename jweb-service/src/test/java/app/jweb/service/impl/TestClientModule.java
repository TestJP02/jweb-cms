package app.jweb.service.impl;

import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestClientModule extends AbstractServiceModule {
    public TestClientModule() {
        super("test.api", Lists.newArrayList("jweb.service"));
    }

    @Override
    protected void configure() {
        api().service(TestWebService.class, options("test", ServiceOptions.class).url);
    }
}
