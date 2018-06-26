package io.sited.user.service;

import io.sited.database.Repository;
import io.sited.user.api.user.ResetPasswordRequest;
import io.sited.user.domain.ResetPasswordToken;
import io.sited.user.domain.User;
import io.sited.util.exception.Exceptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static java.time.OffsetDateTime.now;

/**
 * @author chi
 */
public class ResetPasswordTokenService {
    @Inject
    UserService userService;

    @Inject
    Repository<ResetPasswordToken> repository;

    @Transactional
    public ResetPasswordToken create(ResetPasswordRequest request) {
        Optional<User> user = userService.findByUsername(request.username);
        if (!user.isPresent()) {
            throw Exceptions.badRequestException("username", "user.error.userNoneExists");
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.id = UUID.randomUUID().toString();
        resetPasswordToken.userId = user.get().id;
        resetPasswordToken.token = request.token;
        resetPasswordToken.createdBy = request.requestBy;
        resetPasswordToken.createdTime = now();
        repository.insert(resetPasswordToken);
        return resetPasswordToken;
    }

    public Optional<ResetPasswordToken> find(String userId, String token) {
        return repository.query("SELECT t from ResetPasswordToken t WHERE t.userId=?0 AND t.token=?1", userId, token).findOne();
    }

    @Transactional
    public boolean delete(String id) {
        return repository.delete(id);
    }
}
