package io.sited.user.web.web;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.App;
import io.sited.page.api.PageTemplateWebService;
import io.sited.user.api.OauthUserWebService;
import io.sited.user.api.UserWebService;
import io.sited.user.api.oauth.CreateOauthUserRequest;
import io.sited.user.api.oauth.OauthUserResponse;
import io.sited.user.api.oauth.Provider;
import io.sited.user.api.user.UserResponse;
import io.sited.user.web.UserWebOptions;
import io.sited.user.web.service.Oauth10aService;
import io.sited.user.web.service.Oauth20Service;
import io.sited.user.web.service.OauthResponse;
import io.sited.user.web.service.RequestTokenModel;
import io.sited.user.web.service.UserInfoContextProvider;
import io.sited.user.web.service.UsernameStrategy;
import io.sited.user.web.service.ValidationRules;
import io.sited.util.JSON;
import io.sited.web.AppInfo;
import io.sited.web.Cookies;
import io.sited.web.RequestInfo;
import io.sited.web.SessionInfo;
import io.sited.web.Template;
import io.sited.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

import static io.sited.user.web.service.Oauth10aService.REQUEST_TOKEN_PREFIX;

/**
 * @author chi
 */
@Path("/user")
public class UserController {
    private static final String SESSION_PROVIDER = "provider";
    @Inject
    App app;
    @Inject
    Oauth20Service oauth20Service;
    @Inject
    Oauth10aService oauth10aService;
    @Inject
    UserWebOptions options;
    @Inject
    UserWebService userWebService;
    @Inject
    OauthUserWebService oauthUserWebService;
    @Inject
    SessionInfo sessionInfo;
    @Inject
    UserInfo userInfo;
    @Inject
    AppInfo appInfo;
    @Inject
    RequestInfo requestInfo;
    @Inject
    PageTemplateWebService pageTemplateWebService;

    @Path("/{username}")
    @GET
    public Response center(@PathParam("username") String username) {
        UserResponse user = userWebService.findByUsername(username).orElseThrow(BadRequestException::new);
        Map<String, Object> context = Maps.newHashMap();
        context.put("user", user);
        return Response.ok(Template.of("template/user-center.html", context)).build();
    }

    @Path("/user/profile")
    @GET
    public Response profile(@QueryParam("code") String code, @QueryParam("oauth_verifier") String oauthVerifier) {
        Optional<String> providerOptional = sessionInfo.get(SESSION_PROVIDER);
        if (providerOptional.isPresent()) {
            Provider provider = Provider.valueOf(providerOptional.get());
            if (provider.oauth2)
                return auth2(provider, code, sessionInfo);
            return auth(provider, oauthVerifier, sessionInfo);
        }
        Map<String, Object> context = Maps.newHashMap();
        context.put("user", userInfo);
        return Response.ok(Template.of("template/user-info.html", context)).build();
    }

    private Response auth(Provider provider, String oauthVerifier, SessionInfo session) {
        Optional<String> optional = session.get(REQUEST_TOKEN_PREFIX + provider.name());
        if (!optional.isPresent()) throw new NotFoundException("missing request token,provider=" + provider.name());
        RequestTokenModel model = JSON.fromJSON(optional.get(), RequestTokenModel.class);
        OAuth1RequestToken requestToken = new OAuth1RequestToken(model.token, model.tokenSecret, model.oauthCallbackConfirmed, model.rawResponse);
        OauthResponse oauthResponse = oauth10aService.auth(provider, oauthVerifier, requestToken);
        return response(oauthResponse, session, provider);
    }

    private Response auth2(Provider provider, String code, SessionInfo session) {
        OauthResponse oauthResponse = oauth20Service.auth(provider, code);
        return response(oauthResponse, session, provider);
    }

    private Response response(OauthResponse oauthResponse, SessionInfo session, Provider provider) {
        Optional<OauthUserResponse> oauthUserResponseOptional = Optional.empty();
        if (!Strings.isNullOrEmpty(oauthResponse.username()) && options.usernameStrategy.equals(UsernameStrategy.EMAIL_PHONE)) {
            oauthUserResponseOptional = oauthUserWebService.findByUsername(oauthResponse.username());
        }
        if (!Strings.isNullOrEmpty(oauthResponse.email()) && options.usernameStrategy.equals(UsernameStrategy.EMAIL)) {
            oauthUserResponseOptional = oauthUserWebService.findByEmail(oauthResponse.email());
        }
        if (!Strings.isNullOrEmpty(oauthResponse.phone()) && options.usernameStrategy.equals(UsernameStrategy.PHONE)) {
            oauthUserResponseOptional = oauthUserWebService.findByPhone(oauthResponse.phone());
        }
        if (!oauthUserResponseOptional.isPresent()) {
            CreateOauthUserRequest createOauthUserRequest = new CreateOauthUserRequest();
            createOauthUserRequest.username = oauthResponse.username();
            createOauthUserRequest.email = oauthResponse.email();
            createOauthUserRequest.phone = oauthResponse.phone();
            createOauthUserRequest.provider = provider;
            createOauthUserRequest.requestBy = provider.name();

            //TODO: remove language
            createOauthUserRequest.language = null;
            OauthUserResponse oauthUserResponse = oauthUserWebService.create(createOauthUserRequest);
            Map<String, Object> content = Maps.newHashMap();
            content.put("oauthUserResponse", oauthUserResponse);
            return Response.ok(Template.of("template/user-register-by-oauth.html", ImmutableMap.copyOf(content))).build();
        }
        Optional<UserResponse> userOptional = Optional.empty();
        if (!Strings.isNullOrEmpty(oauthResponse.username()) && options.usernameStrategy.equals(UsernameStrategy.EMAIL_PHONE)) {
            userOptional = userWebService.findByUsername(oauthResponse.username());
        }
        if (!Strings.isNullOrEmpty(oauthResponse.email()) && options.usernameStrategy.equals(UsernameStrategy.EMAIL)) {
            userOptional = userWebService.findByEmail(oauthResponse.email());
        }
        if (!Strings.isNullOrEmpty(oauthResponse.phone()) && options.usernameStrategy.equals(UsernameStrategy.PHONE)) {
            userOptional = userWebService.findByPhone(oauthResponse.phone());
        }
        session.put(UserInfoContextProvider.SESSION_USER_ID, userOptional.get().id);
        return Response.ok(Template.of("template/user-index.html")).build();
    }

    @Path("/login")
    @GET
    public Response login(@QueryParam("provider") Provider provider) {
        if (provider != null) {
            sessionInfo.put(SESSION_PROVIDER, provider.name());
            if (provider.oauth2)
                return Response.temporaryRedirect(URI.create(oauth20Service.redirectUri(provider))).build();
            return Response.temporaryRedirect(URI.create(oauth10aService.redirectUri(provider, sessionInfo))).build();
        }
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        bindings.put("app", appInfo);
        bindings.put("request", requestInfo);
        bindings.put("user", userInfo);
        Map<String, Object> page = Maps.newHashMap();
        page.put("fields", Maps.newHashMap());
        page.put("title", "Login");
        page.put("description", "User Login");
        page.put("keywords", Lists.newArrayList());
        bindings.put("page", page);
        pageTemplateWebService.findByTemplatePath("template/user/login.html")
            .ifPresent(template -> bindings.put("template", template));
        return Response.ok(Template.of("template/login.html", bindings)).build();
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
        bindings.put("app", appInfo);
        bindings.put("request", requestInfo);
        bindings.put("user", userInfo);
        Map<String, Object> page = Maps.newHashMap();
        page.put("fields", Maps.newHashMap());
        page.put("title", "Login");
        page.put("description", "User Login");
        page.put("keywords", Lists.newArrayList());
        bindings.put("page", page);
        Optional<String> pinCodeCountDown = sessionInfo.get("PinCodeCountDown");
        if (pinCodeCountDown.isPresent()) {
            long time = Long.valueOf(pinCodeCountDown.get());
            long current = OffsetDateTime.now().toEpochSecond();
            if (current >= time) {
                bindings.put("countDown", 0);
            } else {
                bindings.put("countDown", time - current);
            }
        } else {
            bindings.put("countDown", 0);
        }
        pageTemplateWebService.findByTemplatePath("template/user/login.html")
            .ifPresent(template -> bindings.put("template", template));
        return Response.ok(Template.of("template/register.html", bindings)).build();
    }

    @Path("/password/reset")
    @GET
    public Response resetPassword() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        bindings.put("app", appInfo);
        bindings.put("request", requestInfo);
        bindings.put("user", userInfo);
        Map<String, Object> page = Maps.newHashMap();
        page.put("fields", Maps.newHashMap());
        page.put("title", "Login");
        page.put("description", "User Login");
        page.put("keywords", Lists.newArrayList());
        bindings.put("page", page);
        pageTemplateWebService.findByTemplatePath("template/user/login.html")
            .ifPresent(template -> bindings.put("template", template));
        return Response.ok(Template.of("template/reset-password.html", bindings)).build();
    }

    @Path("/password/forget")
    @GET
    public Response forgetPassword() {
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("baseURL", app.baseURL());
        bindings.put("app", appInfo);
        bindings.put("request", requestInfo);
        bindings.put("user", userInfo);
        Map<String, Object> page = Maps.newHashMap();
        page.put("fields", Maps.newHashMap());
        page.put("title", "Login");
        page.put("description", "User Login");
        page.put("keywords", Lists.newArrayList());
        bindings.put("page", page);
        pageTemplateWebService.findByTemplatePath("template/user/login.html")
            .ifPresent(template -> bindings.put("template", template));
        return Response.ok(Template.of("template/forget-password.html", bindings)).build();
    }

    @Path("/password/forget/sent")
    @GET
    public Response forgetPasswordSent() {
        return Response.ok(Template.of("template/forget-password-sent.html")).build();
    }
}
