package io.sited.user;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.message.TopicOptions;
import io.sited.user.api.UserAutoLoginTokenWebService;
import io.sited.user.api.UserGroupWebService;
import io.sited.user.api.UserModule;
import io.sited.user.api.UserWebService;
import io.sited.user.api.group.CreateUserGroupRequest;
import io.sited.user.api.group.UserGroupResponse;
import io.sited.user.api.user.CreateUserRequest;
import io.sited.user.api.user.UserChangedMessage;
import io.sited.user.api.user.UserGroupStatus;
import io.sited.user.api.user.UserLoginMessage;
import io.sited.user.api.user.UserPasswordChangedMessage;
import io.sited.user.api.user.UserQuery;
import io.sited.user.api.user.UserRegisterMessage;
import io.sited.user.api.user.UserStatus;
import io.sited.user.domain.ResetPasswordToken;
import io.sited.user.domain.User;
import io.sited.user.domain.UserAutoLoginToken;
import io.sited.user.domain.UserGroup;
import io.sited.user.service.ResetPasswordTokenService;
import io.sited.user.service.UserAutoLoginTokenService;
import io.sited.user.service.UserGroupService;
import io.sited.user.service.UserService;
import io.sited.user.web.UserAutoLoginTokenWebServiceImpl;
import io.sited.user.web.UserGroupWebServiceImpl;
import io.sited.user.web.UserWebServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author chi
 */
public class UserModuleImpl extends UserModule {
    private final Logger logger = LoggerFactory.getLogger(UserModuleImpl.class);

    @Override
    protected void configure() {
        UserOptions options = options("user", UserOptions.class);
        bind(UserOptions.class).toInstance(options);

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(UserGroup.class)
            .entity(ResetPasswordToken.class)
            .entity(UserAutoLoginToken.class)
            .entity(User.class);

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(UserLoginMessage.class, new TopicOptions());
        messageConfig.createTopic(UserRegisterMessage.class, new TopicOptions());
        messageConfig.createTopic(UserChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(UserPasswordChangedMessage.class, new TopicOptions());

        bind(UserService.class);
        bind(UserGroupService.class);
        bind(ResetPasswordTokenService.class);
        bind(UserAutoLoginTokenService.class);

        api().service(UserWebService.class, UserWebServiceImpl.class);
        api().service(UserGroupWebService.class, UserGroupWebServiceImpl.class);
        api().service(UserAutoLoginTokenWebService.class, UserAutoLoginTokenWebServiceImpl.class);
    }

    @Override
    protected void onStartup() {
        UserOptions.DefaultAdminUser defaultAdminUser = require(UserOptions.class).defaultAdminUser;
        if (defaultAdminUser != null) {
            UserWebService userWebService = require(UserWebService.class);
            UserGroupWebService userGroupWebService = require(UserGroupWebService.class);

            Optional<UserGroupResponse> userGroupResponseOptional = userGroupWebService.findByName("admin");
            UserGroupResponse userGroupResponse;
            if (!userGroupResponseOptional.isPresent()) {
                CreateUserGroupRequest createUserGroupRequest = new CreateUserGroupRequest();
                createUserGroupRequest.name = "admin";
                createUserGroupRequest.displayName = "admin";
                createUserGroupRequest.roles = Lists.newArrayList("*");
                createUserGroupRequest.status = UserGroupStatus.ACTIVE;
                userGroupResponse = userGroupWebService.create(createUserGroupRequest);
            } else {
                userGroupResponse = userGroupResponseOptional.get();
            }

            UserQuery query = new UserQuery();
            query.page = 0;
            query.limit = 0;
            if (userWebService.find(query).total == 0) {
                logger.info("create default admin user, username={}", defaultAdminUser.username);

                CreateUserRequest createUserRequest = new CreateUserRequest();
                createUserRequest.username = defaultAdminUser.username;
                createUserRequest.email = defaultAdminUser.email;
                createUserRequest.phone = defaultAdminUser.phone;
                createUserRequest.password = defaultAdminUser.password;
                createUserRequest.description = defaultAdminUser.description;
                createUserRequest.language = app().language();
                createUserRequest.status = UserStatus.ACTIVE;
                createUserRequest.userGroupIds = Lists.newArrayList(userGroupResponse.id);
                userWebService.create(createUserRequest);
            }
        }
    }
}
