package app.jweb.pincode.api;


import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;

/**
 * @author chi
 */
public class PinCodeModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("pincode", ServiceOptions.class);
        api().service(PinCodeWebService.class, options.url);
    }
}
