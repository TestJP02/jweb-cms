package app.jweb.service.impl;

import app.jweb.service.AbstractServiceModule;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestClientModuleImpl extends AbstractServiceModule {
    public TestClientModuleImpl() {
        super("test", Lists.newArrayList("jweb.service"));
    }

    @Override
    protected void configure() {
        api().service(TestWebService.class, TestWebServiceImpl.class);
    }
}
