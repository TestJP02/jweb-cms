package app.jweb.web.impl;


import app.jweb.web.SessionInfo;
import app.jweb.web.SessionManager;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author chi
 */
public class SessionInfoContextProvider implements Provider<SessionInfo> {
    @Inject
    SessionManager sessionManager;

    @Inject
    ContainerRequestContext context;

    @Override
    public SessionInfo get() {
        return sessionManager.get(context);
    }
}
