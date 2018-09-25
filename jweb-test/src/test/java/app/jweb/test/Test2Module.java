package app.jweb.test;

import com.google.common.collect.Lists;
import app.jweb.AbstractModule;
import app.jweb.test.impl.Test2Service;

/**
 * @author chi
 */
public class Test2Module extends AbstractModule {
    public Test2Module() {
        super("test2", Lists.newArrayList("test", "jweb.database"));
    }

    @Override
    protected void configure() {
        bind(Test2Service.class);
    }
}
