package app.jweb.user.web.service;

import app.jweb.user.api.oauth.Provider;

import java.time.OffsetDateTime;

/**
 * @author chi
 */
public interface OauthResponse {
    String nickname();

    String username();

    String email();

    String phone();

    Provider provider();

    OffsetDateTime createdTime();

    OffsetDateTime updatedTime();
}
