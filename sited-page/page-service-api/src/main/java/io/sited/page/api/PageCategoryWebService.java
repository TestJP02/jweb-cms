package io.sited.page.api;


import io.sited.page.api.category.CategoryNodeResponse;
import io.sited.page.api.category.CategoryQuery;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CategoryTreeQuery;
import io.sited.page.api.category.CreateCategoryRequest;
import io.sited.page.api.category.DeleteCategoryRequest;
import io.sited.page.api.category.UpdateCategoryRequest;
import io.sited.util.collection.QueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/api/page/category")
public interface PageCategoryWebService {
    @Path("/tree")
    @PUT
    List<CategoryNodeResponse> tree(CategoryTreeQuery query);

    @Path("/roots")
    @GET
    List<CategoryNodeResponse> roots();

    @Path("/first-two-levels")
    @GET
    List<CategoryNodeResponse> firstTwoLevels();

    @Path("/{id}/sub-tree")
    @GET
    List<CategoryNodeResponse> subTree(@PathParam("id") String id);

    @Path("/sub-tree")
    @PUT
    List<CategoryNodeResponse> subTree(List<String> categoryIds);

    @Path("/{id}")
    @GET
    CategoryResponse get(@PathParam("id") String id);

    @Path("/{id}/parents")
    @GET
    List<CategoryResponse> parents(@PathParam("id") String id);

    @Path("/{id}/children")
    @GET
    List<CategoryResponse> children(@PathParam("id") String id);

    @Path("/find")
    @PUT
    QueryResponse<CategoryResponse> find(CategoryQuery query);

    @Path("/path/{path}")
    @GET
    Optional<CategoryResponse> findByPath(@PathParam("path") String path);

    @POST
    CategoryResponse create(CreateCategoryRequest request);

    @Path("/{id}")
    @PUT
    CategoryResponse update(@PathParam("id") String id, UpdateCategoryRequest request);

    @Path("/batch-delete")
    @POST
    void delete(DeleteCategoryRequest request);
}
