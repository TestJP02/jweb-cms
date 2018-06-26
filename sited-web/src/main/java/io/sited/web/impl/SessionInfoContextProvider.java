package io.sited.web.impl;


import io.sited.web.SessionInfo;
import io.sited.web.SessionManager;

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
