package app.jweb.service;

import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestServiceModule extends AbstractServiceModule {
    public TestServiceModule() {
        super("service.test", Lists.newArrayList("jweb.service"));
    }

    @Override
    protected void configure() {
        api().service(TestService.class, TestServiceImpl.class);
    }
}
