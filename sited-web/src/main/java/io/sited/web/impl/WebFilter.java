package io.sited.web.impl;

import io.sited.App;
import io.sited.web.ClientInfo;
import io.sited.web.Cookies;
import io.sited.web.WebOptions;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author chi
 */
public class WebFilter implements ContainerResponseFilter {
    @Inject
    WebOptions webOptions;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    App app;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Map<String, Cookie> cookies = requestContext.getCookies();
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        if (!cookies.containsKey(webOptions.cookie.clientId)) {
            ClientInfo clientInfo = (ClientInfo) requestContext.getProperty("__client_info");
            String clientId = clientInfo == null ? UUID.randomUUID().toString() : clientInfo.id();
            String language = clientInfo == null ? app.language() : clientInfo.language();
            headers.add("Set-Cookie", new NewCookie(webOptions.cookie.clientId, clientId, "/", null, null, Integer.MAX_VALUE, false));
            headers.add("Set-Cookie", new NewCookie(webOptions.cookie.language, language, "/", null, null, Integer.MAX_VALUE, false));
        }

        SessionInfoImpl sessionInfo = (SessionInfoImpl) requestContext.getProperty("__session_info");
        if (sessionInfo == null) {
            return;
        }

        if (sessionInfo.invalidated) {
            headers.add("Set-Cookie", Cookies.removeCookie(webOptions.cookie.sessionId));
            sessionRepository.remove(sessionInfo.id());
            return;
        }

        if (!cookies.containsKey(webOptions.cookie.sessionId)) {
            headers.add("Set-Cookie", Cookies.newSessionCookie(webOptions.cookie.sessionId, sessionInfo.id()));
        }
    }

}
