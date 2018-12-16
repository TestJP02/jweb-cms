package app.jweb.user.service;

import app.jweb.user.UserOptions;
import app.jweb.user.api.user.BatchGetUserRequest;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.LoginRequest;
import app.jweb.user.api.user.UpdatePasswordRequest;
import app.jweb.user.api.user.UpdateUserRequest;
import app.jweb.user.api.user.UserChangedMessage;
import app.jweb.user.api.user.UserLoginMessage;
import app.jweb.user.api.user.UserPasswordChangedMessage;
import app.jweb.user.api.user.UserQuery;
import app.jweb.user.api.user.UserRegisterMessage;
import app.jweb.user.api.user.UserStatus;
import app.jweb.user.domain.User;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class UserService {
    @Inject
    Repository<User> repository;
    @Inject
    UserOptions userOptions;

    @Inject
    MessagePublisher<UserRegisterMessage> registerMessagePublisher;
    @Inject
    MessagePublisher<UserLoginMessage> loginMessagePublisher;
    @Inject
    MessagePublisher<UserChangedMessage> userChangedMessagePublisher;
    @Inject
    MessagePublisher<UserPasswordChangedMessage> passwordChangedMessagePublisher;

    public User login(LoginRequest request) {
        Optional<User> userOptional = findByUsername(request.username);
        if (!userOptional.isPresent()) {
            throw Exceptions.badRequestException("username", "user.error.userNoneExists");
        }

        User user = userOptional.get();
        if (!user.hashedPassword.equals(new PasswordHasher(user.salt, request.password).hash(user.iteration))) {
            throw Exceptions.badRequestException("username", "user.error.invalidPassword");
        }

        if (user.status == UserStatus.INACTIVE) {
            throw Exceptions.badRequestException("username", "user.error.inactive");
        }

        if (user.status == UserStatus.AUDITING) {
            throw Exceptions.badRequestException("username", "user.error.auditing");
        }

        notifyUserLogin(user);
        return userOptional.get();
    }

    private void notifyUserLogin(User user) {
        UserLoginMessage message = new UserLoginMessage();
        message.id = user.id;
        message.username = user.username;
        message.nickname = user.nickname;
        message.description = user.description;
        message.imageURL = user.imageURL;
        message.email = user.email;
        message.phone = user.phone;
        message.status = user.status;
        message.createdBy = user.createdBy;
        message.createdTime = user.createdTime;
        message.updatedBy = user.updatedBy;
        message.updatedTime = user.updatedTime;
        loginMessagePublisher.publish(message);
    }

    @Transactional
    public User update(String id, UpdateUserRequest request) {
        User user = get(id);

        if (!Strings.isNullOrEmpty(request.username) && !Objects.equals(user.username, request.username)) {
            Optional<User> userOptional = findByUsername(request.username);
            if (userOptional.isPresent()) {
                throw Exceptions.badRequestException("username", "username exists");
            }
            user.username = request.username;
        }
        if (!Strings.isNullOrEmpty(request.phone)) {
            user.phone = request.phone;
        }
        if (!Strings.isNullOrEmpty(request.email)) {
            user.email = request.email;
        }
        if (!Strings.isNullOrEmpty(request.nickname)) {
            user.nickname = request.nickname;
        }
        if (!Strings.isNullOrEmpty(request.imageURL)) {
            user.imageURL = request.imageURL;
        }
        if (request.gender != null) {
            user.gender = request.gender;
        }
        if (!Strings.isNullOrEmpty(request.language)) {
            user.language = request.language;
        }
        if (!Strings.isNullOrEmpty(request.country)) {
            user.country = request.country;
        }
        if (!Strings.isNullOrEmpty(request.city)) {
            user.city = request.city;
        }
        if (!Strings.isNullOrEmpty(request.state)) {
            user.state = request.state;
        }

        if (request.tags != null) {
            user.tags = Joiner.on(",").join(request.tags);
        }
        if (request.userGroupIds != null) {
            user.userGroupIds = Joiner.on(",").join(request.userGroupIds);
        }
        if (request.description != null) {
            user.description = request.description;
        }
        if (request.fields != null) {
            user.fields = JSON.toJSON(request.fields);
        }
        user.updatedTime = OffsetDateTime.now();
        user.updatedBy = request.requestBy;
        repository.update(id, user);

        notifyUserChanged(user);

        return user;
    }

    private void notifyUserChanged(User user) {
        UserChangedMessage message = new UserChangedMessage();
        message.id = user.id;
        message.username = user.username;
        message.nickname = user.nickname;
        message.description = user.description;
        message.imageURL = user.imageURL;
        message.email = user.email;
        message.phone = user.phone;
        message.status = user.status;
        message.createdBy = user.createdBy;
        message.createdTime = user.createdTime;
        message.updatedBy = user.updatedBy;
        message.updatedTime = user.updatedTime;
        userChangedMessagePublisher.publish(message);
    }

    @Transactional
    public User create(CreateUserRequest request) {
        if (isUsernameExists(request.username)) {
            throw Exceptions.badRequestException("username", "user.error.usernameExists");
        }

        if (!Strings.isNullOrEmpty(request.email) && isEmailExits(request.email)) {
            throw Exceptions.badRequestException("email", "user.error.emailExists");
        }

        if (!Strings.isNullOrEmpty(request.phone) && isPhoneExists(request.phone)) {
            throw Exceptions.badRequestException("phone", "user.error.phoneExists");
        }

        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.username = request.username.toLowerCase(Locale.ENGLISH);
        user.nickname = request.nickname;
        if (Strings.isNullOrEmpty(user.nickname)) {
            user.nickname = nickname(user.username);
        }
        user.salt = salt();
        user.iteration = (int) (System.currentTimeMillis() % 4 + 1);
        user.hashedPassword = new PasswordHasher(user.salt, request.password).hash(user.iteration);
        user.language = request.language;
        user.gender = request.gender;
        user.country = request.country;
        user.state = request.state;
        user.city = request.city;
        user.channel = request.channel;
        user.campaign = request.campaign;
        user.tags = request.tags == null ? null : Joiner.on(",").join(request.tags);
        user.userGroupIds = request.userGroupIds == null ? null : Joiner.on(",").join(request.userGroupIds);
        user.createdTime = OffsetDateTime.now();
        user.createdBy = request.requestBy;
        user.updatedTime = OffsetDateTime.now();
        user.updatedBy = request.requestBy;
        user.email = request.email;
        user.phone = request.phone;
        user.imageURL = request.imageURL;
        user.type = request.type;
        user.status = request.status == null ? UserStatus.ACTIVE : request.status;
        user.description = request.description;
        user.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        repository.insert(user);

        notifyUserRegistered(user);
        return user;
    }

    private void notifyUserRegistered(User user) {
        UserRegisterMessage message = new UserRegisterMessage();
        message.id = user.id;
        message.username = user.username;
        message.nickname = user.nickname;
        message.description = user.description;
        message.imageURL = user.imageURL;
        message.email = user.email;
        message.phone = user.phone;
        message.status = user.status;
        message.type = user.type;
        message.language = user.language;
        message.gender = user.gender;
        message.country = user.country;
        message.state = user.state;
        message.city = user.city;
        message.channel = user.channel;
        message.campaign = user.campaign;
        message.createdBy = user.createdBy;
        message.createdTime = user.createdTime;
        message.updatedBy = user.updatedBy;
        message.updatedTime = user.updatedTime;
        registerMessagePublisher.publish(message);
    }

    private String nickname(String username) {
        int index = username.indexOf('@');
        if (index > 0) {
            return username.substring(0, index);
        }
        return "User" + username.hashCode();
    }

    public User get(String id) {
        return repository.get(id);
    }

    public boolean isUsernameExists(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean isEmailExits(String email) {
        return email == null || findByEmail(email).isPresent();
    }


    public boolean isPhoneExists(String phone) {
        return phone == null || findByPhone(phone).isPresent();
    }

    private String salt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return Hashing.sha256().hashBytes(bytes).toString();
    }

    public Optional<User> findByUsername(String username) {
        return repository.query("SELECT t FROM User t WHERE t.username=?0", username.toLowerCase(Locale.ENGLISH)).findOne();
    }

    public Optional<User> findByEmail(String email) {
        return repository.query("SELECT t FROM User t WHERE t.email=?0", email.toLowerCase(Locale.ENGLISH)).findOne();
    }

    public Optional<User> findByPhone(String phone) {
        return repository.query("SELECT t FROM User t WHERE t.phone=?0", phone).findOne();
    }

    public QueryResponse<User> find(UserQuery query) {
        Query<User> dbQuery = repository.query("SELECT t FROM User t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(query.query)) {
            dbQuery.append("AND t.username LIKE ?" + index++, "%" + query.query.toLowerCase(Locale.ENGLISH) + "%");
        }
        if (query.status != null) {
            dbQuery.append("AND t.status=?" + index++, query.status);
        }
        if (!Strings.isNullOrEmpty(query.userGroupId)) {
            dbQuery.append("AND userGroupIds LIKE ?" + index, "%" + query.userGroupId + "%");
        }
        dbQuery.limit(query.page, query.limit);
        if (!Strings.isNullOrEmpty(query.sortingField)) {
            dbQuery.sort("t." + query.sortingField, query.desc);
        }
        return dbQuery.findAll();
    }

    @Transactional
    public User updatePassword(String id, UpdatePasswordRequest request) {
        User user = get(id);
        user.salt = salt();
        user.iteration = (int) (System.currentTimeMillis() % 4 + 1);
        user.hashedPassword = new PasswordHasher(user.salt, request.password).hash(user.iteration);
        user.updatedTime = OffsetDateTime.now();
        user.updatedBy = request.requestBy;
        repository.update(id, user);

        notifyPasswordChanged(user);
        return user;
    }

    private void notifyPasswordChanged(User user) {
        UserPasswordChangedMessage message = new UserPasswordChangedMessage();
        message.id = user.id;
        message.username = user.username;
        message.nickname = user.nickname;
        message.description = user.description;
        message.imageURL = user.imageURL;
        message.email = user.email;
        message.phone = user.phone;
        message.status = user.status;
        message.language = user.language;
        message.createdBy = user.createdBy;
        message.createdTime = user.createdTime;
        message.updatedBy = user.updatedBy;
        message.updatedTime = user.updatedTime;
        passwordChangedMessagePublisher.publish(message);
    }

    @Transactional
    public void delete(String id, String requestBy) {
        User user = get(id);
        if (user.status.equals(UserStatus.INACTIVE)) {
            repository.delete(id);
        } else {
            user.status = UserStatus.INACTIVE;
            user.updatedBy = requestBy;
            user.updatedTime = OffsetDateTime.now();
            repository.update(user.id, user);
        }

        notifyUserChanged(user);
    }

    @Transactional
    public void revert(String id, String requestBy) {
        User user = get(id);
        if (!user.status.equals(UserStatus.INACTIVE)) {
            throw Exceptions.badRequestException("status", "user.error.invalidUserStatus");
        }
        user.status = UserStatus.ACTIVE;
        user.updatedBy = requestBy;
        user.updatedTime = OffsetDateTime.now();
        repository.update(id, user);
    }

    public List<User> batchGet(BatchGetUserRequest request) {
        return repository.batchGet(request.ids);
    }

    @Transactional
    public void updateStatus(String id, UserStatus status, String requestBy) {
        repository.execute("UPDATE User t SET t.status=?0,t.updatedTime=?1,t.updatedBy=?2 WHERE id=?3", status, OffsetDateTime.now(), requestBy, id);
    }
}
