package io.sited.user.web.service;

import io.sited.user.api.oauth.Provider;

import java.time.OffsetDateTime;

/**
 * @author chi
 */
public interface OauthResponse {
    String username();
    String email();
    String phone();
    Provider provider();
    OffsetDateTime createdTime();
    OffsetDateTime updatedTime();
}
