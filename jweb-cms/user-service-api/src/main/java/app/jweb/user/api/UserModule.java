package app.jweb.user.api;


import app.jweb.service.AbstractServiceModule;
import app.jweb.service.ServiceOptions;


/**
 * @author chi
 */
public class UserModule extends AbstractServiceModule {
    @Override
    protected void configure() {
        ServiceOptions options = options("user", ServiceOptions.class);
        api().service(UserGroupWebService.class, options.url);
        api().service(UserWebService.class, options.url);
        api().service(UserAutoLoginTokenWebService.class, options.url);
        api().service(OauthUserWebService.class, options.url);
        api().service(UserStatisticsWebService.class, options.url);
    }
}
