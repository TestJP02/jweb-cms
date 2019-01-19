package app.jweb.user;

import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.message.TopicOptions;
import app.jweb.user.api.OauthUserWebService;
import app.jweb.user.api.UserAutoLoginTokenWebService;
import app.jweb.user.api.UserGroupWebService;
import app.jweb.user.api.UserModule;
import app.jweb.user.api.UserStatisticsWebService;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.group.CreateUserGroupRequest;
import app.jweb.user.api.group.UserGroupResponse;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.UserChangedMessage;
import app.jweb.user.api.user.UserGroupStatus;
import app.jweb.user.api.user.UserLoginMessage;
import app.jweb.user.api.user.UserPasswordChangedMessage;
import app.jweb.user.api.user.UserQuery;
import app.jweb.user.api.user.UserRegisterMessage;
import app.jweb.user.api.user.UserStatus;
import app.jweb.user.domain.OauthUser;
import app.jweb.user.domain.ResetPasswordToken;
import app.jweb.user.domain.User;
import app.jweb.user.domain.UserAutoLoginToken;
import app.jweb.user.domain.UserGroup;
import app.jweb.user.service.OauthUserService;
import app.jweb.user.service.ResetPasswordTokenService;
import app.jweb.user.service.UserAutoLoginTokenService;
import app.jweb.user.service.UserGroupService;
import app.jweb.user.service.UserService;
import app.jweb.user.service.UserStatisticsService;
import app.jweb.user.web.OauthUserWebServiceImpl;
import app.jweb.user.web.UserAutoLoginTokenWebServiceImpl;
import app.jweb.user.web.UserGroupWebServiceImpl;
import app.jweb.user.web.UserStatisticsWebServiceImpl;
import app.jweb.user.web.UserWebServiceImpl;
import com.google.common.collect.Lists;
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
            .entity(User.class)
            .entity(OauthUser.class);

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(UserLoginMessage.class, new TopicOptions());
        messageConfig.createTopic(UserRegisterMessage.class, new TopicOptions());
        messageConfig.createTopic(UserChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(UserPasswordChangedMessage.class, new TopicOptions());

        bind(UserService.class);
        bind(UserGroupService.class);
        bind(ResetPasswordTokenService.class);
        bind(UserAutoLoginTokenService.class);
        bind(OauthUserService.class);
        bind(UserStatisticsService.class);

        api().service(UserWebService.class, UserWebServiceImpl.class);
        api().service(UserGroupWebService.class, UserGroupWebServiceImpl.class);
        api().service(UserAutoLoginTokenWebService.class, UserAutoLoginTokenWebServiceImpl.class);
        api().service(OauthUserWebService.class, OauthUserWebServiceImpl.class);
        api().service(UserStatisticsWebService.class, UserStatisticsWebServiceImpl.class);

        onStartup(this::start);
    }

    private void start() {
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
