package io.sited.database.impl;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import io.sited.database.DatabaseModule;

/**
 * @author chi
 */
public class TestModule extends AbstractModule {
    public TestModule() {
        super("test", Lists.newArrayList("sited.database"));
    }

    @Override
    protected void configure() {
        module(DatabaseModule.class)
            .entity(TestEntity.class);

        bind(TestService.class);
    }
}
