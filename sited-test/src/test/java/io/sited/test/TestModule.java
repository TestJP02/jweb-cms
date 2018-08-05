package io.sited.test;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import io.sited.test.impl.TestService;

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
