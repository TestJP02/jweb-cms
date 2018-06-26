package io.sited.user.web;


import io.sited.cache.CacheModule;
import io.sited.cache.CacheOptions;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.user.api.user.UserChangedMessage;
import io.sited.user.web.component.ForgetPasswordFormComponent;
import io.sited.user.web.component.LoginFormComponent;
import io.sited.user.web.component.RegisterFormComponent;
import io.sited.user.web.component.ResetPasswordFormComponent;
import io.sited.user.web.component.UsernameComponent;
import io.sited.user.web.message.UserChangedMessageHandler;
import io.sited.user.web.service.Oauth10aService;
import io.sited.user.web.service.Oauth20Service;
import io.sited.user.web.service.UserCachedService;
import io.sited.user.web.service.UserCachedView;
import io.sited.user.web.service.UserInfoContextProvider;
import io.sited.user.web.web.UserController;
import io.sited.user.web.web.ajax.UserAJAXController;
import io.sited.user.web.web.interceptor.LoginRequiredInterceptor;
import io.sited.user.web.web.interceptor.RolesAllowedInterceptor;
import io.sited.web.AbstractWebModule;
import io.sited.web.UserInfo;
import io.sited.web.WebOptions;

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

        module(CacheModule.class).create(UserCachedView.class, new CacheOptions());
        bind(UserCachedService.class);

        web().bindRequestFilter(requestInjection(new LoginRequiredInterceptor()));

        bind(Oauth20Service.class);
        bind(Oauth10aService.class);
        web().controller(UserAJAXController.class);
        web().controller(UserController.class);
        message("conf/messages/user");

        web().addComponent(requestInjection(new UsernameComponent()));
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
        messageConfig.listen(UserChangedMessage.class, UserChangedMessageHandler.class);
    }
}
