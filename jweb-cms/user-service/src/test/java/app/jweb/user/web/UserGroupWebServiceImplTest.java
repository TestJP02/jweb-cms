package app.jweb.user.web;

import app.jweb.user.UserModuleImpl;
import app.jweb.user.api.UserGroupWebService;
import app.jweb.user.api.group.DeleteUserGroupRequest;
import app.jweb.user.api.group.BatchGetRequest;
import app.jweb.user.api.group.CreateUserGroupRequest;
import app.jweb.user.api.group.UpdateUserGroupRequest;
import app.jweb.user.api.group.UserGroupQuery;
import app.jweb.user.api.group.UserGroupResponse;
import app.jweb.user.api.user.UserGroupStatus;
import com.google.common.collect.Lists;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.test.MockApp;
import app.jweb.util.collection.QueryResponse;
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
@Install({MessageModule.class, UserModuleImpl.class, DatabaseModule.class, ServiceModule.class})
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
        DeleteUserGroupRequest request = new DeleteUserGroupRequest();
        request.ids = Lists.newArrayList(id);
        request.requestBy = "test";
        ContainerResponse response = app.put("/api/user/group/batch-delete").setEntity(request).execute();
        assertEquals(204, response.getStatus());
    }
}