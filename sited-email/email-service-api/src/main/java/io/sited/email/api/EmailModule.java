package io.sited.email.api;


import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;

/**
 * @author chi
 */
public class EmailModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("email", ServiceOptions.class);
        api().service(EmailWebService.class, options.url);
        api().service(EmailTemplateWebService.class, options.url);
    }
}
