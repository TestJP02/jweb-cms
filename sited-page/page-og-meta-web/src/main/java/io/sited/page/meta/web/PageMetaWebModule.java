package io.sited.page.meta.web;


import io.sited.page.meta.web.service.processor.OgMetaElementProcessor;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageMetaWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addElementProcessor(new OgMetaElementProcessor());
    }
}
