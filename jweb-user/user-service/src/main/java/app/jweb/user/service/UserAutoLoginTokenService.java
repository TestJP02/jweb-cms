package app.jweb.user.service;

import app.jweb.user.domain.UserAutoLoginToken;
import app.jweb.database.Repository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class UserAutoLoginTokenService {
    @Inject
    Repository<UserAutoLoginToken> repository;

    @Transactional
    public UserAutoLoginToken create(String userId, String token, OffsetDateTime expiredTime, String requestBy) {
        UserAutoLoginToken userAutoLoginToken = new UserAutoLoginToken();
        userAutoLoginToken.id = UUID.randomUUID().toString();
        userAutoLoginToken.userId = userId;
        userAutoLoginToken.token = token;
        userAutoLoginToken.expiredTime = expiredTime;
        userAutoLoginToken.createdTime = OffsetDateTime.now();
        userAutoLoginToken.createdBy = requestBy;
        repository.insert(userAutoLoginToken);
        return userAutoLoginToken;
    }

    public Optional<UserAutoLoginToken> findByToken(String token) {
        return repository.query("SELECT t FROM UserAutoLoginToken t WHERE t.token=?0", token).findOne();
    }

    @Transactional
    public void deleteAllByUserId(String userId) {
        repository.execute("DELETE FROM UserAutoLoginToken t WHERE t.userId=?0", userId);
    }

    public Optional<UserAutoLoginToken> findByUserId(String userId) {
        return repository.query("SELECT t FROM UserAutoLoginToken t WHERE t.userId=?0", userId).findOne();
    }
}
