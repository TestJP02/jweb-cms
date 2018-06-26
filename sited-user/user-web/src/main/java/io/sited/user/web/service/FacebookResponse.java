package io.sited.user.web.service;

import io.sited.user.api.oauth.Provider;

import java.time.OffsetDateTime;

/**
 * @author chi
 */
public class FacebookResponse implements OauthResponse {
    @Override
    public String username() {
        return null;
    }

    @Override
    public String email() {
        return null;
    }

    @Override
    public String phone() {
        return null;
    }

    @Override
    public Provider provider() {
        return null;
    }

    @Override
    public OffsetDateTime createdTime() {
        return null;
    }

    @Override
    public OffsetDateTime updatedTime() {
        return null;
    }
}
