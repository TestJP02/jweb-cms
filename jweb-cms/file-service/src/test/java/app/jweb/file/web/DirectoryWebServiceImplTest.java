package app.jweb.file.web;

import app.jweb.file.FileModuleImpl;
import app.jweb.file.api.directory.CreateDirectoriesRequest;
import app.jweb.file.api.directory.DirectoryNodeResponse;
import app.jweb.file.api.directory.DirectoryQuery;
import app.jweb.file.api.directory.DirectoryResponse;
import app.jweb.file.api.directory.UpdateDirectoryRequest;
import app.jweb.database.DatabaseModule;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install({FileModuleImpl.class, DatabaseModule.class, ServiceModule.class})
public class DirectoryWebServiceImplTest {
    @Inject
    MockApp app;
    @Inject
    DirectoryWebService directoryWebService;

    private DirectoryResponse testDirectory;

    @BeforeEach
    public void setup() {
        CreateDirectoriesRequest request = new CreateDirectoriesRequest();
        request.path = "/test/";
        testDirectory = directoryWebService.createDirectories(request);
    }

    @Test
    public void get() {
        ContainerResponse containerResponse = app.get("/api/directory/" + testDirectory.id).execute();
        assertEquals(200, containerResponse.getStatus());
        DirectoryResponse directoryResponse = (DirectoryResponse) containerResponse.getEntity();
        assertEquals("test", directoryResponse.name);
        assertEquals("/test/", directoryResponse.path);
    }

    @Test
    public void testFindByPath() {
        ContainerResponse response = app.get("/api/directory?path=/test/").execute();
        assertEquals(200, response.getStatus());
        Optional<DirectoryResponse> optional = (Optional) response.getEntity();
        assertTrue(optional.isPresent());
        DirectoryResponse directoryResponse = optional.get();
        assertEquals(testDirectory.name, directoryResponse.name);
        assertEquals(testDirectory.path, directoryResponse.path);
    }

    @Test
    void find() {
        DirectoryQuery query = new DirectoryQuery();
        query.page = 1;
        query.limit = 10;
        ContainerResponse containerResponse = app.put("/api/directory").setEntity(query).execute();
        assertEquals(200, containerResponse.getStatus());
        QueryResponse<DirectoryResponse> queryResponse = (QueryResponse) containerResponse.getEntity();
        assertEquals(2, queryResponse.total.intValue());
    }

    @Test
    void children() {
        CreateDirectoryRequest request = new CreateDirectoryRequest();
        request.parentId = testDirectory.id;
        request.path = "/create";
        request.description = "create";
        request.requestBy = UUID.randomUUID().toString();
        DirectoryResponse child = directoryWebService.create(request);
        ContainerResponse containerResponse = app.get("/api/directory/" + testDirectory.id + "/children").execute();
        assertEquals(200, containerResponse.getStatus());
        List<String> list = (List) containerResponse.getEntity();
        assertEquals(1, list.size());
        assertEquals(child.id, list.get(0));
    }

    @Test
    public void testTree() {
        ContainerResponse response = app.get("/api/directory/tree").execute();
        List<DirectoryNodeResponse> directories = (List<DirectoryNodeResponse>) response.getEntity();
        assertEquals(1, directories.size());
        DirectoryNodeResponse first = directories.get(0);
        assertEquals("/", first.directory.path);
        assertEquals(1, first.children.size());
        DirectoryNodeResponse second = first.children.get(0);
        assertEquals("/test/", second.directory.path);
        assertEquals(0, second.children.size());
    }

    @Test
    void firstTwoLevels() {
        ContainerResponse containerResponse = app.get("/api/directory/first-two-levels").execute();
        assertEquals(200, containerResponse.getStatus());
        List<DirectoryNodeResponse> list = (List) containerResponse.getEntity();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).children.size());
        assertEquals(testDirectory.id, list.get(0).children.get(0).directory.id);
    }

    @Test
    void subTree() {
        ContainerResponse containerResponse = app.get("/api/directory/" + testDirectory.id + "/sub-tree").execute();
        assertEquals(200, containerResponse.getStatus());
        List<DirectoryNodeResponse> list = (List) containerResponse.getEntity();
        assertEquals(0, list.size());
    }

    @Test
    void create() {
        CreateDirectoryRequest request = new CreateDirectoryRequest();
        request.parentId = testDirectory.id;
        request.path = "/create/";
        request.description = "create";
        request.requestBy = UUID.randomUUID().toString();
        ContainerResponse containerResponse = app.post("/api/directory").setEntity(request).execute();
        assertEquals(200, containerResponse.getStatus());
        DirectoryResponse directoryResponse = (DirectoryResponse) containerResponse.getEntity();
        assertNotNull(directoryResponse.id);
        assertEquals(request.path, directoryResponse.path);
    }

    @Test
    public void createDirectories() {
        CreateDirectoriesRequest request = new CreateDirectoriesRequest();
        request.path = "/upload/dir/child/";
        ContainerResponse containerResponse = app.post("/api/directory/create").setEntity(request).execute();
        assertEquals(200, containerResponse.getStatus());
        DirectoryResponse directoryResponse = (DirectoryResponse) containerResponse.getEntity();
        assertNotNull(directoryResponse.parentId);
        assertEquals("/upload/dir/child/", directoryResponse.path);
    }

    @Test
    public void testUpdate() {
        UpdateDirectoryRequest request = new UpdateDirectoryRequest();
        request.description = "description";
        request.requestBy = "test";
        ContainerResponse response = app.put("/api/directory/" + testDirectory.id).setEntity(request).execute();
        DirectoryResponse directoryResponse = (DirectoryResponse) response.getEntity();
        assertEquals("description", directoryResponse.description);
    }

    @Test
    void delete() {
        ContainerResponse containerResponse = app.delete("/api/directory/" + testDirectory.id + "?requestBy=" + UUID.randomUUID().toString()).execute();
        assertEquals(204, containerResponse.getStatus());
    }
}