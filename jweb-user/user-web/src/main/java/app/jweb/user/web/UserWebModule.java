package app.jweb.user.web;


import app.jweb.user.api.user.UserChangedMessage;
import app.jweb.user.web.web.UserController;
import app.jweb.cache.CacheModule;
import app.jweb.cache.CacheOptions;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.user.web.service.Oauth10aService;
import app.jweb.user.web.service.Oauth20Service;
import app.jweb.user.web.service.UserCacheService;
import app.jweb.user.web.service.UserCacheView;
import app.jweb.user.web.service.UserInfoContextProvider;
import app.jweb.user.web.service.WebNotAuthorizedExceptionHandler;
import app.jweb.user.web.service.component.ForgetPasswordFormComponent;
import app.jweb.user.web.service.component.LoginFormComponent;
import app.jweb.user.web.service.component.RegisterFormComponent;
import app.jweb.user.web.service.component.ResetPasswordFormComponent;
import app.jweb.user.web.service.component.UserComponent;
import app.jweb.user.web.service.component.UsernameComponent;
import app.jweb.user.web.service.message.UserChangedMessageHandler;
import app.jweb.user.web.web.ajax.UserAJAXController;
import app.jweb.user.web.web.interceptor.LoginRequiredInterceptor;
import app.jweb.user.web.web.interceptor.RolesAllowedInterceptor;
import app.jweb.web.AbstractWebModule;
import app.jweb.web.UserInfo;
import app.jweb.web.WebOptions;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.DynamicFeature;

/**
 * @author chi
 */
public class UserWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        bind(WebOptions.class).toInstance(app().options("web", WebOptions.class));
        UserWebOptions options = options("user-web", UserWebOptions.class);
        bind(UserWebOptions.class).toInstance(options);

        module(CacheModule.class).create(UserCacheView.class, new CacheOptions());
        bind(UserCacheService.class);
        bind(Oauth10aService.class);
        bind(Oauth20Service.class);

        web().bindExceptionMapper(requestInjection(new WebNotAuthorizedExceptionHandler()));
        web().bindRequestFilter(requestInjection(new LoginRequiredInterceptor()));

        web().controller(UserAJAXController.class);
        web().controller(UserController.class);
        message("conf/messages/user");

        web().addComponent(requestInjection(new UsernameComponent()));
        web().addComponent(requestInjection(new UserComponent()));
        web().addComponent(requestInjection(new LoginFormComponent()));
        web().addComponent(requestInjection(new RegisterFormComponent()));
        web().addComponent(requestInjection(new ForgetPasswordFormComponent()));
        web().addComponent(requestInjection(new ResetPasswordFormComponent()));

        web().bind(UserInfo.class, UserInfoContextProvider.class);

        app().register((DynamicFeature) (resourceInfo, context) -> {
            if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
                context.register(RolesAllowedInterceptor.class);
            }
        });

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.listen(UserChangedMessage.class, requestInjection(new UserChangedMessageHandler()));
    }
}
