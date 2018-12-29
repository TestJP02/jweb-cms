package app.jweb.database.impl;

import app.jweb.AbstractModule;
import app.jweb.database.DatabaseModule;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestModule extends AbstractModule {
    public TestModule() {
        super("test", Lists.newArrayList("jweb.database"));
    }

    @Override
    protected void configure() {
        module(DatabaseModule.class)
            .entity(TestEntity.class);

        bind(TestService.class);
    }
}
