package app.jweb.page.share.addthis;


import app.jweb.page.share.addthis.service.component.AddthisShareButtonsComponent;
import app.jweb.web.AbstractWebModule;

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
