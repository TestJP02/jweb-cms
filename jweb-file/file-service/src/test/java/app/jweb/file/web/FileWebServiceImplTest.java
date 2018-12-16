package app.jweb.file.web;

import app.jweb.file.FileModuleImpl;
import com.google.common.collect.Lists;
import app.jweb.database.DatabaseModule;
import app.jweb.file.api.DirectoryWebService;
import app.jweb.file.api.FileWebService;
import app.jweb.file.api.directory.CreateDirectoryRequest;
import app.jweb.file.api.file.BatchDeleteFileRequest;
import app.jweb.file.api.file.CreateFileRequest;
import app.jweb.file.api.file.FileListQuery;
import app.jweb.file.api.file.FileListResponse;
import app.jweb.file.api.file.FileQuery;
import app.jweb.file.api.file.FileResponse;
import app.jweb.file.api.file.FileStatus;
import app.jweb.file.api.file.UpdateFileRequest;
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
public class FileWebServiceImplTest {
    @Inject
    MockApp app;
    @Inject
    FileWebService fileWebService;
    @Inject
    DirectoryWebService directoryWebService;
    String directoryId;
    FileResponse file;

    @BeforeEach
    public void setup() {
        CreateDirectoryRequest createDirectoryRequest = new CreateDirectoryRequest();
        createDirectoryRequest.path = "/file/";
        createDirectoryRequest.requestBy = "test";
        directoryId = directoryWebService.create(createDirectoryRequest).id;
        CreateFileRequest request = new CreateFileRequest();
        request.directoryId = directoryId;
        request.path = "test";
        request.description = "test";
        request.requestBy = "test";
        file = fileWebService.create(request);
    }

    @Test
    public void get() {
        ContainerResponse response = app.get("/api/file/" + file.id).execute();
        assertEquals("test", ((FileResponse) response.getEntity()).description);
    }

    @Test
    public void findByPath() {
        ContainerResponse containerResponse = app.get("/api/file?path=" + file.path).execute();
        assertEquals(200, containerResponse.getStatus());
        Optional<FileResponse> optional = (Optional) containerResponse.getEntity();
        assertTrue(optional.isPresent());
        FileResponse fileResponse = optional.get();
        assertEquals(file.id, fileResponse.id);
        assertEquals(file.description, fileResponse.description);
    }

    @Test
    public void find() {
        FileQuery query = new FileQuery();
        query.page = 1;
        query.limit = 10;
        ContainerResponse response = app.put("/api/file").setEntity(query).execute();
        QueryResponse<FileResponse> result = (QueryResponse) response.getEntity();
        assertEquals(1, result.total.intValue());
        assertEquals("test", result.items.get(0).description);
    }

    @Test
    void list() {
        FileListQuery fileListQuery = new FileListQuery();
        fileListQuery.limit = 10;
        fileListQuery.page = 1;
        ContainerResponse containerResponse = app.put("/api/file/list").setEntity(fileListQuery).execute();
        assertEquals(200, containerResponse.getStatus());
        QueryResponse<FileListResponse> queryResponse = (QueryResponse) containerResponse.getEntity();
        assertTrue(queryResponse.total.intValue() >= 2);
        queryResponse.items.forEach(fileListResponse -> {
            if (!fileListResponse.isDirectory) {
                assertEquals(file.description, fileListResponse.description);
            }
        });
    }

    @Test
    void create() {
        CreateFileRequest request = new CreateFileRequest();
        request.directoryId = directoryId;
        request.path = "create";
        request.fileName = "create";
        request.description = "create";
        request.requestBy = UUID.randomUUID().toString();
        ContainerResponse containerResponse = app.post("/api/file").setEntity(request).execute();
        assertEquals(200, containerResponse.getStatus());
        FileResponse fileResponse = (FileResponse) containerResponse.getEntity();
        assertNotNull(fileResponse.id);
        assertEquals(request.path, fileResponse.path);
    }

    @Test
    public void update() {
        UpdateFileRequest request = new UpdateFileRequest();
        request.description = "update";
        request.requestBy = "test";
        ContainerResponse response = app.put("/api/file/" + file.id).setEntity(request).execute();
        FileResponse fileResponse = (FileResponse) response.getEntity();
        assertEquals("update", fileResponse.description);
    }

    @Test
    void deleteAndRevert() {
        ContainerResponse containerResponse = app.delete("/api/file/" + file.id + "?requestBy=" + UUID.randomUUID().toString()).execute();
        assertEquals(204, containerResponse.getStatus());
        FileResponse fileResponse = fileWebService.get(file.id);
        assertEquals(FileStatus.INACTIVE, fileResponse.status);
        containerResponse = app.put("/api/file/" + file.id + "/revert?requestBy=" + UUID.randomUUID().toString()).execute();
        assertEquals(204, containerResponse.getStatus());
        fileResponse = fileWebService.get(file.id);
        assertEquals(FileStatus.ACTIVE, fileResponse.status);
    }

    @Test
    public void batchDelete() {
        CreateFileRequest request = new CreateFileRequest();
        request.directoryId = directoryId;
        request.path = "test";
        request.description = "test";
        request.requestBy = "test";
        String newId = fileWebService.create(request).id;

        BatchDeleteFileRequest batchDeleteFileRequest = new BatchDeleteFileRequest();
        batchDeleteFileRequest.ids = Lists.newArrayList(file.id, newId);
        batchDeleteFileRequest.requestBy = "test";
        app.post("/api/file/batch-delete").setEntity(batchDeleteFileRequest).execute();

        FileResponse response = fileWebService.get(file.id);
        assertEquals(FileStatus.INACTIVE, response.status);
    }
}