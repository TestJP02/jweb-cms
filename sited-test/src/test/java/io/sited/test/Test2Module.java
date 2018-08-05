package io.sited.test;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import io.sited.test.impl.Test2Service;

/**
 * @author chi
 */
public class Test2Module extends AbstractModule {
    public Test2Module() {
        super("test2", Lists.newArrayList("test", "sited.database"));
    }

    @Override
    protected void configure() {
        bind(Test2Service.class);
    }
}
