package io.sited.file.api;

import io.sited.file.api.directory.CreateDirectoriesRequest;
import io.sited.file.api.directory.CreateDirectoryRequest;
import io.sited.file.api.directory.DirectoryNodeResponse;
import io.sited.file.api.directory.DirectoryQuery;
import io.sited.file.api.directory.DirectoryResponse;
import io.sited.file.api.directory.UpdateDirectoryRequest;
import io.sited.util.collection.QueryResponse;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/directory")
public interface DirectoryWebService {
    @Path("/{id}")
    @GET
    @RolesAllowed("GET")
    DirectoryResponse get(@PathParam("id") String id);

    @GET
    @RolesAllowed("LIST")
    Optional<DirectoryResponse> findByPath(@QueryParam("path") String path);

    @PUT
    @RolesAllowed("LIST")
    QueryResponse<DirectoryResponse> find(DirectoryQuery query);

    @Path("/{id}/children")
    @GET
    @RolesAllowed("LIST")
    List<String> children(@PathParam("id") String id);

    @Path("/tree")
    @GET
    @RolesAllowed("LIST")
    List<DirectoryNodeResponse> tree();

    @Path("/first-two-levels")
    @GET
    @RolesAllowed("LIST")
    List<DirectoryNodeResponse> firstTwoLevels();

    @Path("/{id}/sub-tree")
    @GET
    @RolesAllowed("LIST")
    List<DirectoryNodeResponse> subTree(@PathParam("id") String id);

    @POST
    @RolesAllowed("CREATE")
    DirectoryResponse create(CreateDirectoryRequest request);

    @Path("/create")
    @POST
    @RolesAllowed("CREATE")
    DirectoryResponse createDirectories(CreateDirectoriesRequest request);

    @Path("/{id}")
    @PUT
    @RolesAllowed("UPDATE")
    DirectoryResponse update(@PathParam("id") String id, UpdateDirectoryRequest request);

    @Path("/{id}")
    @DELETE
    @RolesAllowed("DELETE")
    void delete(@PathParam("id") String id, @QueryParam("requestBy") String requestBy);
}
