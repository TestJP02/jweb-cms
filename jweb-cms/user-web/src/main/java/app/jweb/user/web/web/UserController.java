package app.jweb.user.web.web;

import app.jweb.App;
import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.PageInfo;
import app.jweb.user.api.OauthUserWebService;
import app.jweb.user.api.oauth.OauthLoginRequest;
import app.jweb.user.api.oauth.OauthLoginResponse;
import app.jweb.user.api.oauth.Provider;
import app.jweb.user.web.UserWebOptions;
import app.jweb.user.web.service.Oauth10aService;
import app.jweb.user.web.service.Oauth20Service;
import app.jweb.user.web.service.OauthResponse;
import app.jweb.user.web.service.RequestTokenModel;
import app.jweb.user.web.service.UserInfoContextProvider;
import app.jweb.user.web.service.ValidationRules;
import app.jweb.util.JSON;
import app.jweb.util.i18n.MessageBundle;
import app.jweb.web.Cookies;
import app.jweb.web.SessionInfo;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

import static app.jweb.user.web.service.Oauth10aService.REQUEST_TOKEN_PREFIX;
import static app.jweb.user.web.web.ajax.UserAJAXController.COOKIE_FROM_URL;

/**
 * @author chi
 */
@Path("/user")
public class UserController extends AbstractPageController {
    private static final String SESSION_PROVIDER = "provider";
    @Inject
    App app;
    @Inject
    UserWebOptions options;
    @Inject
    SessionInfo sessionInfo;
    @Inject
    MessageBundle messageBundle;
    @Inject
    Oauth10aService oauth10aService;
    @Inject
    Oauth20Service oauth20Service;
    @Inject
    UserWebOptions userWebOptions;
    @Inject
    OauthUserWebService oauthUserWebService;
    @Inject
    ContainerRequestContext requestContext;

    @Path("/login")
    @GET
    public Response login() {
        Map<String, Object> bindings = Maps.newHashMap();
        String title = messageBundle.get("user.login", clientInfo.language()).orElse("Login");
        PageInfo page = PageInfo.builder()
            .setTitle(title)
            .setDescription(title)
            .setTemplatePath("template/login.html")
            .build();

        return page(page, bindings);
    }

    @Path("/login/{provider}")
    @GET
    public Response loginByProvider(@PathParam("provider") Provider provider) {
        sessionInfo.put(SESSION_PROVIDER, provider.name());
        if (provider.oauth2) {
            return Response.temporaryRedirect(URI.create(oauth20Service.redirectUri(provider))).build();
        } else {
            return Response.temporaryRedirect(URI.create(oauth10aService.redirectUri(provider))).build();
        }
    }

    @Path("/oauth/callback")
    @GET
    public Response oauth(@QueryParam("code") String code, @QueryParam("oauth_verifier") String oauthVerifier) {
        String sessionProvider = sessionInfo.get(SESSION_PROVIDER).orElseThrow(() -> new NotFoundException("missing session provider"));
        Provider provider = Provider.valueOf(sessionProvider);
        OauthResponse oauthResponse;
        if (provider.oauth2) {
            oauthResponse = auth2(provider, code);
        } else {
            oauthResponse = auth(provider, oauthVerifier);
        }
        oauthLogin(oauthResponse);
        Cookie url = requestContext.getCookies().get(COOKIE_FROM_URL);
        if (url != null) {
            return Response.temporaryRedirect(URI.create("")).cookie(Cookies.removeCookie(COOKIE_FROM_URL)).build();
        }
        return Response.temporaryRedirect(URI.create(app.baseURL())).cookie(Cookies.removeCookie(COOKIE_FROM_URL)).build();
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

    private OauthResponse auth(Provider provider, String oauthVerifier) {
        Optional<String> optional = sessionInfo.get(REQUEST_TOKEN_PREFIX + provider.name());
        if (!optional.isPresent()) throw new NotFoundException("missing request token,provider=" + provider.name());
        RequestTokenModel model = JSON.fromJSON(optional.get(), RequestTokenModel.class);
        OAuth1RequestToken requestToken = new OAuth1RequestToken(model.token, model.tokenSecret, model.oauthCallbackConfirmed, model.rawResponse);
        return oauth10aService.auth(provider, oauthVerifier, requestToken);
    }

    private OauthResponse auth2(Provider provider, String code) {
        return oauth20Service.auth(provider, code);
    }

    private void oauthLogin(OauthResponse oauthResponse) {
        OauthLoginRequest oauthLoginRequest = new OauthLoginRequest();
        oauthLoginRequest.email = oauthResponse.email();
        oauthLoginRequest.phone = oauthResponse.phone();
        oauthLoginRequest.username = oauthResponse.username();
        oauthLoginRequest.nickname = oauthResponse.nickname();
        oauthLoginRequest.provider = oauthResponse.provider();
        oauthLoginRequest.autoLogin = true;
        oauthLoginRequest.requestBy = "user-web";
        OauthLoginResponse oauthLoginResponse = oauthUserWebService.login(oauthLoginRequest);
        sessionInfo.put(UserInfoContextProvider.SESSION_USER_ID, oauthLoginResponse.user.id);
    }
}
