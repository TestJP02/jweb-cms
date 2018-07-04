package io.sited.pincode.web;

import io.sited.pincode.web.component.PinCodeComponent;
import io.sited.pincode.web.service.PinCodeProvider;
import io.sited.pincode.web.web.ajax.PinCodeAJAXController;
import io.sited.web.AbstractWebModule;

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
