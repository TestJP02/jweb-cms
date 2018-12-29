package app.jweb.test.impl;

import com.google.common.collect.Lists;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class TestWebModule extends AbstractWebModule {
    public TestWebModule() {
        super("test", Lists.newArrayList("jweb.web"));
    }

    @Override
    protected void configure() {
        web().controller(TestController.class);
    }
}
