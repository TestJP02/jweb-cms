package io.sited.user.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.UserModuleImpl;
import io.sited.user.api.UserWebService;
import io.sited.user.api.user.ApplyPasswordRequest;
import io.sited.user.api.user.BatchGetUserRequest;
import io.sited.user.api.user.CreateUserRequest;
import io.sited.user.api.user.LoginRequest;
import io.sited.user.api.user.LoginResponse;
import io.sited.user.api.user.ResetPasswordRequest;
import io.sited.user.api.user.TokenLoginRequest;
import io.sited.user.api.user.UpdatePasswordRequest;
import io.sited.user.api.user.UpdateUserRequest;
import io.sited.user.api.user.UserQuery;
import io.sited.user.api.user.UserResponse;
import io.sited.user.api.user.UserStatus;
import io.sited.util.collection.QueryResponse;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class})
public class UserWebServiceImplTest {
    @Inject
    UserWebService userWebService;
    @Inject
    MockApp app;

    String id;

    @BeforeEach
    public void setup() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.email = "1@2";
        createUserRequest.username = "some";
        createUserRequest.password = "some";
        id = userWebService.create(createUserRequest).id;
    }

    @Test
    public void login() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "some";
        loginRequest.password = "some";
        loginRequest.autoLogin = true;
        loginRequest.requestBy = "test";
        ContainerResponse response = app.post("/api/user/login").setEntity(loginRequest).execute();
        assertEquals(200, response.getStatus());

        LoginResponse loginResponse = (LoginResponse) response.getEntity();
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.token = loginResponse.autoLoginToken;
        tokenLoginRequest.requestBy = "test";
        response = app.post("/api/user/login-token").setEntity(tokenLoginRequest).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void get() {
        UserResponse response = (UserResponse) app.get("/api/user/" + id).execute().getEntity();
        assertEquals("some", response.username);
    }

    @Test
    public void batchGet() {
        BatchGetUserRequest request = new BatchGetUserRequest();
        request.ids = Lists.newArrayList(id);
        List response = (List) app.put("/api/user/batch-get").setEntity(request).execute().getEntity();
        assertEquals(1, response.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void find() {
        UserQuery query = new UserQuery();
        query.query = "some";
        query.page = 1;
        query.limit = 10;
        QueryResponse<UserResponse> response = (QueryResponse<UserResponse>) app.put("/api/user").setEntity(query).execute().getEntity();
        assertEquals(1, response.total.intValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByUsername() {
        Optional<UserResponse> response = (Optional<UserResponse>) app.get("/api/user/username/some").execute().getEntity();
        assertTrue(response.isPresent());
    }

    @Test
    public void create() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = "test";
        createUserRequest.password = "test";
        createUserRequest.email = "test@11.com";
        createUserRequest.phone = "1111111";
        createUserRequest.status = UserStatus.ACTIVE;
        createUserRequest.requestBy = "test";
        ContainerResponse response = app.post("/api/user").setEntity(createUserRequest).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void update() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.requestBy = "Test";
        ContainerResponse response = app.put("/api/user/" + id).setEntity(updateUserRequest).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updatePassword() {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.password = "1111";
        request.requestBy = "test";
        ContainerResponse response = app.put("/api/user/" + id + "/password").setEntity(request).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void batchDelete() {
    }

    @Test
    public void resetPassword() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.username = "some";
        resetPasswordRequest.token = "test";
        resetPasswordRequest.requestBy = "test";
        ContainerResponse response = app.put("/api/user/password/reset").setEntity(resetPasswordRequest).execute();
        assertEquals(200, response.getStatus());

        ApplyPasswordRequest applyPasswordRequest = new ApplyPasswordRequest();
        applyPasswordRequest.username = "some";
        applyPasswordRequest.pinCode = "test";
        applyPasswordRequest.newPassword = "111";
        applyPasswordRequest.requestBy = "Test";
        response = app.put("/api/user/password/reset/apply").setEntity(applyPasswordRequest).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void deleteAutoLoginToken() {
        ContainerResponse response = app.delete("/api/user/" + id + "/auto-login-token").execute();
        assertEquals(200, response.getStatus());
    }
}