package app.jweb.user.web.service;

import app.jweb.user.api.oauth.Provider;
import app.jweb.user.web.UserWebOptions;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import app.jweb.user.web.exception.WebNotAuthorizedException;
import app.jweb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

/**
 * @author chi
 */
public class Oauth20Service {
    private final Logger logger = LoggerFactory.getLogger(Oauth20Service.class);
    private final ConcurrentMap<Provider, OAuth20Service> services = Maps.newConcurrentMap();

    @Inject
    UserWebOptions options;

    public String redirectUri(Provider provider) {
        return service(provider).getAuthorizationUrl();
    }

    public OauthResponse auth(Provider provider, String code) {
        OAuth20Service service = service(provider);
        Response response;
        try {
            OAuth2AccessToken accessToken = service.getAccessToken(code);
            response = getUserInfo(accessToken, provider, service);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebNotAuthorizedException(null, "login failure", e);
        }
        if (response.getCode() != 200) {
            throw new WebNotAuthorizedException(null, "login failure");
        }
        try {
            Class oauthResponse = response(provider);
            return JSON.fromJSON(response.getBody(), oauthResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Response getUserInfo(OAuth2AccessToken accessToken, Provider provider, OAuth20Service service) throws Exception {
        OAuthRequest request;
        switch (provider) {
            case GITHUB:
            case GOOGLE:
            case FACEBOOK:
                request = new OAuthRequest(Verb.GET, provider.protectedResourceUrl);
                service.signRequest(accessToken, request);
                return service.execute(request);
            case WEIBO:
                request = new OAuthRequest(Verb.GET, "https://api.weibo.com/2/account/get_uid.json");
                service.signRequest(accessToken, request);
                Response response = service.execute(request);
                if (response.getCode() != 200) {
                    throw new WebNotAuthorizedException(null, "login failure");
                }
                WeiboUidResponse uid = JSON.fromJSON(response.getBody(), WeiboUidResponse.class);
                request = new OAuthRequest(Verb.GET, String.format(provider.protectedResourceUrl, uid.uid));
                service.signRequest(accessToken, request);
                return service.execute(request);
            default:
                throw new RuntimeException(String.format("provider %s undefined", provider.name()));
        }
    }

    private OAuth20Service service(Provider provider) {
        OauthStrategy strategy = strategy(provider);
        String secretState = "secret" + new Random().nextInt(999_999);
        OAuth20Service service = services.get(provider);
        if (service == null) {
            ServiceBuilder serviceBuilder = new ServiceBuilder(strategy.clientId)
                .apiSecret(strategy.clientSecret)
                .state(secretState)
                .callback(strategy.callback);
            if (!Strings.isNullOrEmpty(provider.scope)) {
                serviceBuilder.scope(provider.scope);
            }
            service = serviceBuilder
                .build(instance(provider));
            services.put(provider, service);
        }
        return service;
    }

    private DefaultApi20 instance(Provider provider) {
        switch (provider) {
            case GITHUB:
                return GitHubApi.instance();
            case GOOGLE:
                return GoogleApi20.instance();
            case FACEBOOK:
                return FacebookApi.instance();
            case WEIBO:
                return SinaWeiboApi20.instance();
            default:
                throw new RuntimeException(String.format("provider %s undefined", provider.name()));
        }
    }

    private OauthStrategy strategy(Provider provider) {
        switch (provider) {
            case GITHUB:
                return options.github;
            case GOOGLE:
                return options.google;
            case FACEBOOK:
                return options.facebook;
            case WEIBO:
                return options.weibo;
            default:
                throw new RuntimeException(String.format("provider %s undefined", provider.name()));
        }
    }

    private Class response(Provider provider) {
        switch (provider) {
            case GITHUB:
                return GitHubResponse.class;
            case GOOGLE:
                return GoogleResponse.class;
            case FACEBOOK:
                return FacebookResponse.class;
            case WEIBO:
                return WeiboResponse.class;
            default:
                throw new RuntimeException(String.format("provider %s undefined", provider.name()));
        }
    }
}
