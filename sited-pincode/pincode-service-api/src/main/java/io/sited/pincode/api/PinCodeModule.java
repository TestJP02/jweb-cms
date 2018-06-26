package io.sited.pincode.api;


import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

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
