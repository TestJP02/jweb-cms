package io.sited.user.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.email.smtp.SMTPModule;
import io.sited.message.MessageModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.test.MockApp;
import io.sited.user.UserModuleImpl;
import io.sited.user.api.UserGroupWebService;
import io.sited.user.api.group.BatchDeleteUserGroupRequest;
import io.sited.user.api.group.BatchGetRequest;
import io.sited.user.api.group.CreateUserGroupRequest;
import io.sited.user.api.group.UpdateUserGroupRequest;
import io.sited.user.api.group.UserGroupQuery;
import io.sited.user.api.group.UserGroupResponse;
import io.sited.user.api.user.UserGroupStatus;
import io.sited.util.collection.QueryResponse;
import org.glassfish.jersey.server.ContainerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class, SMTPModule.class})
public class UserGroupWebServiceImplTest {
    @Inject
    MockApp app;
    @Inject
    UserGroupWebService userGroupWebService;

    String id;

    @BeforeEach
    public void setup() {
        CreateUserGroupRequest createUserGroupRequest = new CreateUserGroupRequest();
        createUserGroupRequest.name = "Test";
        createUserGroupRequest.status = UserGroupStatus.ACTIVE;
        createUserGroupRequest.roles = Lists.newArrayList("test");
        createUserGroupRequest.requestBy = "Test";
        id = userGroupWebService.create(createUserGroupRequest).id;
    }

    @Test
    public void get() {
        ContainerResponse response = app.get("/api/user/group/" + id).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void batchGet() {
        BatchGetRequest request = new BatchGetRequest();
        request.ids = Lists.newArrayList(id);
        ContainerResponse response = app.put("/api/user/group/batch-get").setEntity(request).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void find() {
        UserGroupQuery query = new UserGroupQuery();
        query.page = 1;
        query.limit = 10;
        QueryResponse<UserGroupResponse> response = (QueryResponse<UserGroupResponse>) app.put("/api/user/group/find").setEntity(query).execute().getEntity();
        assertEquals(1, response.total.intValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAll() {
        List<UserGroupResponse> responses = (List<UserGroupResponse>) app.get("/api/user/group/all").execute().getEntity();
        assertEquals(1, responses.size());
    }

    @Test
    public void update() {
        UpdateUserGroupRequest updateUserGroupRequest = new UpdateUserGroupRequest();
        updateUserGroupRequest.requestBy = "Test";
        ContainerResponse response = app.put("/api/user/group/" + id).setEntity(updateUserGroupRequest).execute();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void batchDelete() {
        BatchDeleteUserGroupRequest request = new BatchDeleteUserGroupRequest();
        request.ids = Lists.newArrayList(id);
        request.requestBy = "test";
        ContainerResponse response = app.put("/api/user/group/batch-delete").setEntity(request).execute();
        assertEquals(204, response.getStatus());
    }
}