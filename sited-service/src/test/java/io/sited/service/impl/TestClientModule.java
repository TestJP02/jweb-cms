package io.sited.service.impl;

import com.google.common.collect.Lists;
import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class TestClientModule extends AbstractServiceModule {
    public TestClientModule() {
        super("test.api", Lists.newArrayList("sited.service"));
    }

    @Override
    protected void configure() {
        api().service(TestWebService.class, options("test", ServiceOptions.class).url);
    }
}
