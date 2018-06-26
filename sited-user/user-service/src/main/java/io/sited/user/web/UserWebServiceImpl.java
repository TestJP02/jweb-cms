package io.sited.user.web;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.ApplyPasswordRequest;
import io.sited.user.api.user.BatchDeleteUserRequest;
import io.sited.user.api.user.BatchGetUserRequest;
import io.sited.user.api.user.CreateUserRequest;
import io.sited.user.api.user.LoginRequest;
import io.sited.user.api.user.LoginResponse;
import io.sited.user.api.user.ResetPasswordRequest;
import io.sited.user.api.user.ResetPasswordResponse;
import io.sited.user.api.user.TokenLoginRequest;
import io.sited.user.api.user.UpdatePasswordRequest;
import io.sited.user.api.user.UpdateUserRequest;
import io.sited.user.api.user.UserQuery;
import io.sited.user.api.user.UserResponse;
import io.sited.user.api.user.UserView;
import io.sited.user.domain.ResetPasswordToken;
import io.sited.user.domain.User;
import io.sited.user.domain.UserAutoLoginToken;
import io.sited.user.service.ResetPasswordTokenService;
import io.sited.user.service.UserAutoLoginTokenService;
import io.sited.user.service.UserService;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;
import io.sited.util.exception.Exceptions;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class UserWebServiceImpl implements UserWebService {
    @Inject
    UserAutoLoginTokenService userAutoLoginTokenService;
    @Inject
    UserService userService;
    @Inject
    ResetPasswordTokenService resetPasswordTokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userService.login(request);
        LoginResponse response = new LoginResponse();
        response.user = response(user);
        if (Boolean.TRUE.equals(request.autoLogin)) {
            Optional<UserAutoLoginToken> autoLoginToken = userAutoLoginTokenService.findByUserId(user.id);
            if (autoLoginToken.isPresent()) {
                response.autoLoginToken = autoLoginToken.get().token;
            } else {
                UserAutoLoginToken userAutoLoginToken = userAutoLoginTokenService.create(user.id, UUID.randomUUID().toString(), null, request.requestBy);
                response.autoLoginToken = userAutoLoginToken.token;
            }
        }
        return response;
    }

    @Override
    public LoginResponse login(TokenLoginRequest request) {
        Optional<UserAutoLoginToken> autoLoginTokenOptional = userAutoLoginTokenService.findByToken(request.token);
        if (!autoLoginTokenOptional.isPresent()) {
            throw Exceptions.badRequestException("invalid token, token={}", request.token);
        }
        UserAutoLoginToken userAutoLoginToken = autoLoginTokenOptional.get();
        User user = userService.get(userAutoLoginToken.userId);

        LoginResponse response = new LoginResponse();
        response.user = response(user);
        response.autoLoginToken = userAutoLoginToken.token;
        return response;
    }

    @Override
    public UserResponse get(String id) {
        return response(userService.get(id));
    }

    @Override
    public List<UserView> batchGet(BatchGetUserRequest request) {
        return userService.batchGet(request).stream().map(this::view).collect(Collectors.toList());
    }

    @Override
    public QueryResponse<UserResponse> find(UserQuery query) {
        return userService.find(query).map(this::response);
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(this::response);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userService.findByEmail(email).map(this::response);
    }

    @Override
    public Optional<UserResponse> findByPhone(String phone) {
        return userService.findByPhone(phone).map(this::response);
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        User user = userService.create(request);
        return response(user);
    }

    @Override
    public UserResponse update(String id, UpdateUserRequest request) {
        return response(userService.update(id, request));
    }

    @Override
    public UserResponse updatePassword(String id, UpdatePasswordRequest request) {
        User user = userService.updatePassword(id, request);
        return response(user);
    }

    @Override
    public void batchDelete(BatchDeleteUserRequest request) {
        request.ids.forEach(id -> {
            userService.delete(id, request.requestBy);
        });
    }

    @Override
    public void revert(String id, String requestBy) {
        userService.revert(id, requestBy);
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userService.findByUsername(request.username);

        if (!userOptional.isPresent()) {
            throw Exceptions.badRequestException("username", "user.error.userNoneExists");
        }

        String userId = userOptional.get().id;
        ResetPasswordToken token = resetPasswordTokenService.create(request);
        ResetPasswordResponse response = new ResetPasswordResponse();
        response.code = token.token;
        response.userId = userId;
        return response;
    }

    @Override
    public void applyPassword(ApplyPasswordRequest request) {
        Optional<User> userOptional = userService.findByUsername(request.username);

        if (!userOptional.isPresent()) {
            throw Exceptions.badRequestException("username", "user.error.userNoneExists");
        }

        String userId = userOptional.get().id;
        Optional<ResetPasswordToken> resetPasswordToken = resetPasswordTokenService.find(userId, request.pinCode);
        if (!resetPasswordToken.isPresent()) {
            throw Exceptions.badRequestException("pinCode", "user.error.invalidPinCode");
        }

        resetPasswordTokenService.delete(resetPasswordToken.get().id);

        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.password = request.newPassword;
        updatePasswordRequest.requestBy = request.requestBy;
        userService.updatePassword(userId, updatePasswordRequest);
    }

    @Override
    public void deleteAutoLoginToken(String id) {
        userAutoLoginTokenService.deleteAllByUserId(id);
    }

    private UserResponse response(User user) {
        UserResponse response = new UserResponse();
        response.id = user.id;
        response.username = user.username;
        response.nickname = user.nickname;
        response.userGroupIds = user.userGroupIds == null ? ImmutableList.of() : Splitter.on(",").splitToList(user.userGroupIds);
        response.email = user.email;
        response.phone = user.phone;
        response.imageURL = user.imageURL;
        response.type = user.type;
        response.language = user.language;
        response.gender = user.gender;
        response.country = user.country;
        response.state = user.state;
        response.city = user.city;
        response.channel = user.channel;
        response.campaign = user.campaign;
        response.tags = user.tags == null ? ImmutableList.of() : Splitter.on(",").splitToList(user.tags);
        response.status = user.status;
        response.description = user.description;
        response.createdTime = user.createdTime;
        response.createdBy = user.createdBy;
        response.updatedTime = user.updatedTime;
        response.updatedBy = user.updatedBy;
        response.fields = user.fields == null ? ImmutableMap.of() : JSON.fromJSON(user.fields, Map.class);
        return response;
    }

    private UserView view(User user) {
        UserView view = new UserView();
        view.id = user.id;
        view.username = user.username;
        view.nickname = user.nickname;
        view.imageURL = user.imageURL;
        view.status = user.status;
        view.description = user.description;
        view.createdTime = user.createdTime;
        return view;
    }
}
