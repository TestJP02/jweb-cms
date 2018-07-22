package io.sited.service;

import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestServiceModule extends AbstractServiceModule {
    public TestServiceModule() {
        super("service.test", Lists.newArrayList("sited.service"));
    }

    @Override
    protected void configure() {
        api().service(TestService.class, TestServiceImpl.class);
    }
}
