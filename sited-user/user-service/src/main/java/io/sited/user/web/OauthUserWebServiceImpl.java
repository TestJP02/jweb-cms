package io.sited.user.web;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import io.sited.user.api.OauthUserWebService;
import io.sited.user.api.oauth.CreateOauthUserRequest;
import io.sited.user.api.oauth.OauthLoginRequest;
import io.sited.user.api.oauth.OauthLoginResponse;
import io.sited.user.api.oauth.OauthUserResponse;
import io.sited.user.api.user.UserResponse;
import io.sited.user.domain.OauthUser;
import io.sited.user.domain.User;
import io.sited.user.domain.UserAutoLoginToken;
import io.sited.user.service.OauthUserService;
import io.sited.user.service.UserAutoLoginTokenService;
import io.sited.user.service.UserService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class OauthUserWebServiceImpl implements OauthUserWebService {
    @Inject
    OauthUserService oauthUserService;
    @Inject
    UserService userService;
    @Inject
    UserAutoLoginTokenService userAutoLoginTokenService;

    @Override
    public Optional<OauthUserResponse> findByUsername(String username) {
        return oauthUserService.findByUsername(username).map(this::response);
    }

    @Override
    public Optional<OauthUserResponse> findByEmail(String email) {
        return oauthUserService.findByEmail(email).map(this::response);
    }

    @Override
    public Optional<OauthUserResponse> findByPhone(String phone) {
        return oauthUserService.findByPhone(phone).map(this::response);
    }

    @Override
    public OauthUserResponse create(CreateOauthUserRequest request) {
        return response(oauthUserService.create(request));
    }

    @Override
    public OauthLoginResponse login(OauthLoginRequest request) {
        //TODO: move strategy to service
        Optional<OauthUser> oauthUser = oauthUserService.findByUsername(request.username);
        if (!oauthUser.isPresent()) {
            oauthUser = oauthUserService.findByEmail(request.email);
        }
        if (!oauthUser.isPresent()) {
            oauthUser = oauthUserService.findByPhone(request.phone);
        }

        User user;
        if (oauthUser.isPresent()) {
            user = userService.get(oauthUser.get().userId);
        } else {
            OauthUser createdUser = oauthUserService.create(request);
            user = userService.get(createdUser.userId);
        }
        OauthLoginResponse response = new OauthLoginResponse();
        if (Boolean.TRUE.equals(request.autoLogin)) {
            Optional<UserAutoLoginToken> autoLoginToken = userAutoLoginTokenService.findByUserId(user.id);
            if (autoLoginToken.isPresent()) {
                response.autoLoginToken = autoLoginToken.get().token;
            } else {
                UserAutoLoginToken userAutoLoginToken = userAutoLoginTokenService.create(user.id, UUID.randomUUID().toString(), null, request.requestBy);
                response.autoLoginToken = userAutoLoginToken.token;
            }
        }
        response.user = response(user);
        response.bindNeeded = false;
        return response;
    }

    private OauthUserResponse response(OauthUser request) {
        OauthUserResponse instance = new OauthUserResponse();
        instance.userId = request.userId;
        instance.username = request.username;
        instance.email = request.email;
        instance.phone = request.phone;
        instance.provider = request.provider;
        instance.createdTime = request.createdTime;
        instance.createdBy = request.createdBy;
        instance.updatedTime = request.updatedTime;
        instance.updatedBy = request.updatedBy;
        return instance;
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
        return response;
    }
}
