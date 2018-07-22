package io.sited.test.impl;

import com.google.common.collect.Lists;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class TestWebModule extends AbstractWebModule {
    public TestWebModule() {
        super("test", Lists.newArrayList("sited.web"));
    }

    @Override
    protected void configure() {
        web().controller(TestController.class);
    }
}
