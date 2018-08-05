package io.sited.page.share.addthis;


import io.sited.page.share.addthis.service.component.AddthisShareButtonsComponent;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class AddthisPageShareWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        AddthisOptions options = options("addthis", AddthisOptions.class);
        web().addComponent(requestInjection(new AddthisShareButtonsComponent(options)));
    }
}
