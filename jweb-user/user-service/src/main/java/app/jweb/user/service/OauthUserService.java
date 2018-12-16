package app.jweb.user.service;


import app.jweb.user.api.oauth.CreateOauthUserRequest;
import app.jweb.user.api.oauth.OauthLoginRequest;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.UserStatus;
import app.jweb.user.domain.OauthUser;
import app.jweb.database.Repository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class OauthUserService {
    @Inject
    Repository<OauthUser> repository;
    @Inject
    UserService userService;

    public Optional<OauthUser> findByUsername(String username) {
        return repository.query("SELECT t from OauthUser t WHERE t.username=?0", username).findOne();
    }

    public Optional<OauthUser> findByEmail(String email) {
        return repository.query("SELECT t from OauthUser t WHERE t.email=?0", email).findOne();
    }

    public Optional<OauthUser> findByPhone(String phone) {
        return repository.query("SELECT t from OauthUser t WHERE t.phone=?0", phone).findOne();
    }

    @Transactional
    public OauthUser create(CreateOauthUserRequest request) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = request.username;
        createUserRequest.nickname = request.nickname;
        createUserRequest.email = request.email;
        createUserRequest.phone = request.phone;
        createUserRequest.requestBy = request.requestBy;
        createUserRequest.status = UserStatus.ACTIVE;
        createUserRequest.type = request.provider.name();
        createUserRequest.language = request.language;
        createUserRequest.gender = request.gender;
        createUserRequest.country = request.country;
        createUserRequest.state = request.state;
        createUserRequest.city = request.city;
        createUserRequest.imageURL = request.imageURL;
        createUserRequest.channel = request.channel;
        createUserRequest.campaign = request.campaign;
        String id = userService.create(createUserRequest).id;

        OauthUser oauthUser = new OauthUser();
        oauthUser.id = UUID.randomUUID().toString();
        oauthUser.userId = id;
        oauthUser.username = request.username;
        oauthUser.email = request.email;
        oauthUser.phone = request.phone;
        oauthUser.provider = request.provider;
        oauthUser.createdBy = request.requestBy;
        oauthUser.createdTime = OffsetDateTime.now();
        oauthUser.updatedBy = request.requestBy;
        oauthUser.updatedTime = OffsetDateTime.now();
        return repository.insert(oauthUser);
    }

    @Transactional
    public OauthUser create(OauthLoginRequest request) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = request.username;
        createUserRequest.nickname = request.nickname;
        createUserRequest.email = request.email;
        createUserRequest.phone = request.phone;
        createUserRequest.requestBy = request.requestBy;
        createUserRequest.status = UserStatus.ACTIVE;
        createUserRequest.type = request.provider.name();
        createUserRequest.language = request.language;
        createUserRequest.gender = request.gender;
        createUserRequest.country = request.country;
        createUserRequest.state = request.state;
        createUserRequest.city = request.city;
        createUserRequest.imageURL = request.imageURL;
        createUserRequest.channel = request.channel;
        createUserRequest.campaign = request.campaign;
        String id = userService.create(createUserRequest).id;

        OauthUser oauthUser = new OauthUser();
        oauthUser.id = UUID.randomUUID().toString();
        oauthUser.userId = id;
        oauthUser.username = request.username;
        oauthUser.email = request.email;
        oauthUser.phone = request.phone;
        oauthUser.provider = request.provider;
        oauthUser.createdBy = request.requestBy;
        oauthUser.createdTime = OffsetDateTime.now();
        oauthUser.updatedBy = request.requestBy;
        oauthUser.updatedTime = OffsetDateTime.now();
        return repository.insert(oauthUser);
    }
}
