package app.jweb.user.web;

import app.jweb.user.api.UserAutoLoginTokenWebService;
import app.jweb.user.api.token.CreateUserAutoLoginTokenRequest;
import app.jweb.user.api.token.DeleteUserAutoLoginTokenRequest;
import app.jweb.user.api.token.UserAutoLoginTokenResponse;
import app.jweb.user.domain.UserAutoLoginToken;
import app.jweb.user.service.UserAutoLoginTokenService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class UserAutoLoginTokenWebServiceImpl implements UserAutoLoginTokenWebService {
    @Inject
    UserAutoLoginTokenService userAutoLoginTokenService;

    @Override
    public Optional<UserAutoLoginTokenResponse> find(String userId) {
        Optional<UserAutoLoginToken> userAutoLoginToken = userAutoLoginTokenService.findByUserId(userId);
        return userAutoLoginToken.map(this::response);
    }

    @Override
    public void delete(DeleteUserAutoLoginTokenRequest request) {
        userAutoLoginTokenService.deleteAllByUserId(request.userId);
    }

    @Override
    public UserAutoLoginTokenResponse create(CreateUserAutoLoginTokenRequest request) {
        Optional<UserAutoLoginToken> userAutoLoginTokenOptional = userAutoLoginTokenService.findByUserId(request.userId);
        if (userAutoLoginTokenOptional.isPresent()) {
            return response(userAutoLoginTokenOptional.get());
        }
        UserAutoLoginToken userAutoLoginToken = userAutoLoginTokenService.create(request.userId, UUID.randomUUID().toString(), request.expireTime, request.requestBy);
        return response(userAutoLoginToken);
    }

    private UserAutoLoginTokenResponse response(UserAutoLoginToken instance) {
        UserAutoLoginTokenResponse response = new UserAutoLoginTokenResponse();
        response.id = instance.id;
        response.userId = instance.userId;
        response.token = instance.token;
        response.expiredTime = instance.expiredTime;
        response.createdTime = instance.createdTime;
        response.createdBy = instance.createdBy;
        return response;
    }
}
