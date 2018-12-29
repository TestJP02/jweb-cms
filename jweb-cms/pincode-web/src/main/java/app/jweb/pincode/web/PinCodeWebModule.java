package app.jweb.pincode.web;

import app.jweb.pincode.web.component.PinCodeComponent;
import app.jweb.pincode.web.service.PinCodeProvider;
import app.jweb.pincode.web.web.ajax.PinCodeAJAXController;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class PinCodeWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        PinCodeWebOptions options = options("pincode-web", PinCodeWebOptions.class);

        bind(PinCodeWebOptions.class).toInstance(options);
        web().bind(PinCode.class, PinCodeProvider.class);
        web().controller(PinCodeAJAXController.class);
        web().addComponent(requestInjection(new PinCodeComponent()));
    }
}
