package app.jweb.page.meta.web;

import app.jweb.page.meta.web.service.component.OgMetaComponent;
import app.jweb.page.meta.web.service.processor.OgMetaElementProcessor;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageOgMetaWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addElementProcessor(new OgMetaElementProcessor());
        web().addComponent(new OgMetaComponent());
    }
}
