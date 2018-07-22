package io.sited.user.web;


import io.sited.cache.CacheModule;
import io.sited.cache.CacheOptions;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.user.api.user.UserChangedMessage;
import io.sited.user.web.service.UserCacheService;
import io.sited.user.web.service.UserCacheView;
import io.sited.user.web.service.UserInfoContextProvider;
import io.sited.user.web.service.component.ForgetPasswordFormComponent;
import io.sited.user.web.service.component.LoginFormComponent;
import io.sited.user.web.service.component.RegisterFormComponent;
import io.sited.user.web.service.component.ResetPasswordFormComponent;
import io.sited.user.web.service.component.UserComponent;
import io.sited.user.web.service.component.UsernameComponent;
import io.sited.user.web.service.message.UserChangedMessageHandler;
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

        module(CacheModule.class).create(UserCacheView.class, new CacheOptions());
        bind(UserCacheService.class);

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
