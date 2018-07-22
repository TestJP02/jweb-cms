package io.sited.user.web.web;

import com.google.common.collect.Maps;
import io.sited.App;
import io.sited.page.web.AbstractPageWebController;
import io.sited.page.web.PageInfo;
import io.sited.user.web.UserWebOptions;
import io.sited.user.web.service.ValidationRules;
import io.sited.util.i18n.MessageBundle;
import io.sited.web.Cookies;
import io.sited.web.SessionInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/user")
public class UserController extends AbstractPageWebController {
    @Inject
    App app;
    @Inject
    UserWebOptions options;
    @Inject
    SessionInfo sessionInfo;
    @Inject
    MessageBundle messageBundle;

    @Path("/login")
    @GET
    public Response login() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());

        String title = messageBundle.get("user.login", clientInfo.language()).orElse("Login");
        PageInfo page = PageInfo.builder()
            .setTitle(title)
            .setDescription(title)
            .setTemplatePath("template/login.html")
            .build();

        return page(page, bindings);
    }

    @Path("/logout")
    @GET
    public Response logout() {
        sessionInfo.invalidate();
        return Response.temporaryRedirect(URI.create("/")).cookie(Cookies.removeCookie(options.autoLoginCookie)).build();
    }

    @Path("/register")
    @GET
    public Response register() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("userNameStrategy", options.usernameStrategy);
        ValidationRules validationRules = options.validationRules;
        if (validationRules == null) {
            validationRules = new ValidationRules();
        }
        bindings.put("validationRules", validationRules);
        bindings.put("baseURL", app.baseURL());

        Optional<String> pinCodeCountDown = sessionInfo.get("PinCodeCountDown");
        if (pinCodeCountDown.isPresent()) {
            long time = Long.parseLong(pinCodeCountDown.get());
            long current = OffsetDateTime.now().toEpochSecond();
            if (current >= time) {
                bindings.put("countDown", 0);
            } else {
                bindings.put("countDown", time - current);
            }
        } else {
            bindings.put("countDown", 0);
        }

        String title = messageBundle.get("user.register", clientInfo.language()).orElse("Register");
        PageInfo page = PageInfo.builder()
            .setTitle(title)
            .setDescription(title)
            .setTemplatePath("template/register.html")
            .build();

        return page(page, bindings);
    }

    @Path("/password/reset")
    @GET
    public Response resetPassword() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        String title = messageBundle.get("user.resetPassword", clientInfo.language()).orElse("Reset Password");
        PageInfo page = PageInfo.builder()
            .setTitle(title)
            .setDescription(title)
            .setTemplatePath("template/reset-password.html")
            .build();

        return page(page, bindings);
    }

    @Path("/password/forget")
    @GET
    public Response forgetPassword() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        String title = messageBundle.get("user.forgetPassword", clientInfo.language()).orElse("Forget Password");
        PageInfo page = PageInfo.builder()
            .setTitle(title)
            .setTemplatePath("template/forget-password.html")
            .setDescription(title)
            .build();

        return page(page, bindings);
    }
}
