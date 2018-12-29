package app.jweb.user.web.service;

import app.jweb.user.api.UserGroupWebService;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.group.BatchGetRequest;
import app.jweb.user.api.group.UserGroupResponse;
import app.jweb.user.api.user.LoginResponse;
import app.jweb.user.api.user.TokenLoginRequest;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.web.UserWebOptions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import app.jweb.web.ClientInfo;
import app.jweb.web.SessionInfo;
import app.jweb.web.SessionManager;
import app.jweb.web.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author chi
 */
@Priority(Priorities.USER - 1000)
public class UserInfoContextProvider implements Provider<UserInfo> {
    private final Logger logger = LoggerFactory.getLogger(UserInfoContextProvider.class);

    public static final String PROPERTY_USER_INFO = "__user_info";
    public static final String SESSION_USER_ID = "USER_ID";

    @Inject
    ClientInfo clientInfo;
    @Inject
    ContainerRequestContext requestContext;
    @Inject
    SessionManager sessionManager;
    @Inject
    UserWebService userWebService;
    @Inject
    UserWebOptions userWebOptions;
    @Inject
    UserGroupWebService userGroupWebService;

    @Override
    public UserInfo get() {
        UserInfo userInfo = userInfo(requestContext).orElse(defaultUser(clientInfo.ip()));
        requestContext.setProperty(PROPERTY_USER_INFO, userInfo);
        requestContext.setSecurityContext(userInfo);
        return userInfo;
    }

    private Optional<UserInfo> userInfo(ContainerRequestContext requestContext) {
        UserInfo userInfo = (UserInfo) requestContext.getProperty(PROPERTY_USER_INFO);
        if (userInfo != null) {
            return Optional.of(userInfo);
        }

        SessionInfo sessionInfo = sessionManager.get(requestContext);
        Optional<String> userId = sessionInfo.get(SESSION_USER_ID);
        if (userId.isPresent()) {
            userInfo = user(userWebService.get(userId.get()));
            requestContext.setProperty(PROPERTY_USER_INFO, userInfo);
            requestContext.setSecurityContext(userInfo);
            return Optional.of(userInfo);
        }

        if (userWebOptions.autoLoginEnabled) {
            Optional<UserInfoImpl> userInfoOptional = tryAutoLogin(requestContext, sessionInfo);
            if (userInfoOptional.isPresent()) {
                userInfo = userInfoOptional.get();
                requestContext.setProperty(PROPERTY_USER_INFO, userInfo);
                requestContext.setSecurityContext(userInfo);
                return Optional.of(userInfo);
            }
        }
        return Optional.empty();
    }

    private Optional<UserInfoImpl> tryAutoLogin(ContainerRequestContext request, SessionInfo session) {
        Cookie cookie = request.getCookies().get(userWebOptions.autoLoginCookie);
        if (cookie != null) {
            try {
                TokenLoginRequest authenticationRequest = new TokenLoginRequest();
                authenticationRequest.token = cookie.getValue();
                LoginResponse authenticationResponse = userWebService.login(authenticationRequest);
                UserInfoImpl user = user(authenticationResponse.user);
                session.put(SESSION_USER_ID, user.id());
                return Optional.of(user);
            } catch (Throwable e) {
                logger.warn("invalid auto login token cookie, value={}", cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private UserInfoImpl user(UserResponse response) {
        UserInfoImpl user = new UserInfoImpl();
        user.id = response.id;
        user.username = response.username;
        user.nickname = response.nickname;
        user.phone = response.phone;
        user.email = response.email;
        user.imageURL = response.imageURL;
        user.authenticated = true;

        BatchGetRequest request = new BatchGetRequest();
        request.ids = response.userGroupIds;
        List<UserGroupResponse> groups = userGroupWebService.batchGet(request);
        Set<String> roles = Sets.newHashSet();
        for (UserGroupResponse group : groups) {
            roles.addAll(group.roles);
        }
        user.roles = roles;
        return user;
    }

    private UserInfoImpl defaultUser(String ip) {
        UserInfoImpl user = new UserInfoImpl();
        user.authenticated = false;
        user.roles = ImmutableSet.of();
        user.username = ip;
        return user;
    }
}
