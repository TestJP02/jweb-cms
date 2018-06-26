package io.sited.file.api;

import io.sited.file.api.file.BatchDeleteFileRequest;
import io.sited.file.api.file.CreateFileRequest;
import io.sited.file.api.file.FileListQuery;
import io.sited.file.api.file.FileListResponse;
import io.sited.file.api.file.FileQuery;
import io.sited.file.api.file.FileResponse;
import io.sited.file.api.file.UpdateFileRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/file")
public interface FileWebService {
    @Path("/{id}")
    @GET
    FileResponse get(@PathParam(value = "id") String id);

    @GET
    Optional<FileResponse> findByPath(@QueryParam("path") String path);

    @PUT
    QueryResponse<FileResponse> find(FileQuery query);

    @Path("/list")
    @PUT
    QueryResponse<FileListResponse> list(FileListQuery query);

    @POST
    FileResponse create(CreateFileRequest request);

    @Path("/{id}")
    @PUT
    FileResponse update(@PathParam("id") String id, UpdateFileRequest request);

    @Path("/{id}")
    @DELETE
    void delete(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @Path("/batch-delete")
    @POST
    void batchDelete(BatchDeleteFileRequest request);

    @Path("/{id}/revert")
    @PUT
    void revert(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);

    @Path("/synchronize")
    @GET
    void synchronize();
}
