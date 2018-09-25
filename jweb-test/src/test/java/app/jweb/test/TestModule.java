package app.jweb.test;

import com.google.common.collect.Lists;
import app.jweb.AbstractModule;
import app.jweb.test.impl.TestService;

/**
 * @author chi
 */
public class TestModule extends AbstractModule {
    public TestModule() {
        super("test", Lists.newArrayList());
    }

    @Override
    protected void configure() {
        bind(TestService.class);
    }
}
