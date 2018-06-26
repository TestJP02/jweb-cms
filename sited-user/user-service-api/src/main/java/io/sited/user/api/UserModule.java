package io.sited.user.api;


import io.sited.service.AbstractServiceModule;
import io.sited.service.ServiceOptions;


/**
 * @author chi
 */
public class UserModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("user", ServiceOptions.class);
        api().service(UserGroupWebService.class, options.url);
        api().service(UserWebService.class, options.url);
        api().service(OauthUserWebService.class, options.url);
        api().service(UserAutoLoginTokenWebService.class, options.url);
    }
}
