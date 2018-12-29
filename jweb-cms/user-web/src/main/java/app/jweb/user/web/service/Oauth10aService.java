package app.jweb.user.web.service;

import app.jweb.user.api.oauth.Provider;
import app.jweb.user.web.UserWebOptions;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import app.jweb.user.web.exception.WebNotAuthorizedException;
import app.jweb.util.JSON;
import app.jweb.web.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/**
 * @author chi
 */
public class Oauth10aService {
    private final Logger logger = LoggerFactory.getLogger(Oauth10aService.class);

    public static final String REQUEST_TOKEN_PREFIX = "request_token_";
    private final ConcurrentMap<Provider, OAuth10aService> services = Maps.newConcurrentMap();

    @Inject
    UserWebOptions options;
    @Inject
    SessionInfo sessionInfo;

    public String redirectUri(Provider provider) {
        OAuth10aService service = service(provider);
        try {
            OAuth1RequestToken requestToken = service.getRequestToken();
            sessionInfo.put(REQUEST_TOKEN_PREFIX + provider.name(), JSON.toJSON(requestToken));
            return service.getAuthorizationUrl(requestToken);
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new NotAuthorizedException("login failure", e);
        }
    }

    public OauthResponse auth(Provider provider, String oauthVerifier, OAuth1RequestToken requestToken) {
        OAuth10aService service = service(provider);
        Response response;
        try {
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
            OAuthRequest request = new OAuthRequest(Verb.GET, provider.protectedResourceUrl);
            service.signRequest(accessToken, request);
            response = service.execute(request);
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new WebNotAuthorizedException(null, "login failure", e);
        }
        if (response.getCode() != 200) {
            throw new WebNotAuthorizedException(null, "login failure");
        }
        try {
            Class oauthResponse = response(provider);
            String json = response.getBody();
            logger.info("oauth response, provider={}, json={}", provider, json);
            return JSON.fromJSON(json, oauthResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OAuth10aService service(Provider provider) {
        OauthStrategy strategy = strategy(provider);
        String secretState = "secret" + new Random().nextInt(999_999);
        OAuth10aService service = services.get(provider);
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

    private DefaultApi10a instance(Provider provider) {
        if (provider == Provider.TWITTER) {
            return TwitterApi.instance();
        }
        throw new RuntimeException(String.format("provider %s undefined", provider.name()));
    }

    private OauthStrategy strategy(Provider provider) {
        if (provider == Provider.TWITTER) {
            return options.twitter;
        }
        throw new RuntimeException(String.format("provider %s undefined", provider.name()));
    }

    private Class response(Provider provider) {
        if (provider == Provider.TWITTER) {
            return TwitterResponse.class;
        }
        throw new RuntimeException(String.format("provider %s undefined", provider.name()));
    }
}
