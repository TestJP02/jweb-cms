package io.sited.service.impl;

import com.google.common.collect.Lists;
import io.sited.service.AbstractServiceModule;

/**
 * @author chi
 */
public class TestClientModuleImpl extends AbstractServiceModule {
    public TestClientModuleImpl() {
        super("test", Lists.newArrayList("sited.service"));
    }

    @Override
    protected void configure() {
        api().service(TestWebService.class, TestWebServiceImpl.class);
    }
}
