package app.jweb.user.web;

import app.jweb.user.api.OauthUserWebService;
import app.jweb.user.api.oauth.CreateOauthUserRequest;
import app.jweb.user.api.oauth.OauthLoginRequest;
import app.jweb.user.api.oauth.OauthLoginResponse;
import app.jweb.user.api.oauth.OauthUserResponse;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.domain.OauthUser;
import app.jweb.user.domain.User;
import app.jweb.user.domain.UserAutoLoginToken;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import app.jweb.user.service.OauthUserService;
import app.jweb.user.service.UserAutoLoginTokenService;
import app.jweb.user.service.UserService;

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
