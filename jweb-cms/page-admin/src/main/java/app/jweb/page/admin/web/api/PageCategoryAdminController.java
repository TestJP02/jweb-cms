package app.jweb.page.admin.web.api;


import app.jweb.page.admin.web.api.category.CategoryAJAXResponse;
import app.jweb.page.admin.web.api.category.CreateCategoryAJAXRequest;
import app.jweb.page.admin.web.api.category.DeleteCategoryAJAXRequest;
import app.jweb.page.api.PageCategoryWebService;
import app.jweb.page.api.category.CategoryNodeResponse;
import app.jweb.page.api.category.CategoryResponse;
import app.jweb.page.api.category.CategoryTreeQuery;
import app.jweb.page.api.category.CreateCategoryRequest;
import app.jweb.page.api.category.DeleteCategoryRequest;
import app.jweb.page.api.category.UpdateCategoryRequest;
import app.jweb.web.UserInfo;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.List;

/**
 * @author chi
 */
@Path("/admin/api/page/category")
public class PageCategoryAdminController {
    @Inject
    UserInfo userInfo;
    @Inject
    PageCategoryWebService pageCategoryWebService;
//    @Inject
//    PageCategoryAccessService pageCategoryAccessService;


    @RolesAllowed("LIST")
    @Path("/tree")
    @PUT
    public List<CategoryNodeResponse> tree(CategoryTreeQuery query) {
        return pageCategoryWebService.tree(query);
    }

    @RolesAllowed("LIST")
    @Path("/roots")
    @PUT
    public List<CategoryNodeResponse> roots() {
        CategoryTreeQuery query = new CategoryTreeQuery();
        query.parentId = null;
        query.level = 1;
        return pageCategoryWebService.tree(query);
    }

    @RolesAllowed("LIST")
    @Path("/first-two-levels")
    @GET
    public List<CategoryNodeResponse> firstTwoLevels() {
        CategoryTreeQuery query = new CategoryTreeQuery();
        query.parentId = null;
        query.level = 2;
        return pageCategoryWebService.tree(query);
    }

    @RolesAllowed("LIST")
    @Path("/{id}/sub-tree")
    @PUT
    public List<CategoryNodeResponse> subTree(@PathParam("id") String id) {
        CategoryTreeQuery query = new CategoryTreeQuery();
        query.parentId = id;
        return pageCategoryWebService.tree(query);
    }

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public CategoryAJAXResponse get(@PathParam("id") String id) {
//        return response(pageCategoryAccessService.auth(request.userInfo(), id, "GET",
//            () -> categoryWebService.get(id)));
//        return response(pageCategoryAccessService.auth(request.userInfo(), id, "GET",
//            () -> categoryWebService.get(id)));
        return response(pageCategoryWebService.get(id));
    }

    @RolesAllowed("CREATE")
    @POST
    public CategoryAJAXResponse create(CreateCategoryAJAXRequest createCategoryAJAXRequest) throws IOException {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.parentId = createCategoryAJAXRequest.parentId;
        createCategoryRequest.displayName = createCategoryAJAXRequest.displayName;
        createCategoryRequest.displayOrder = createCategoryAJAXRequest.displayOrder;
        createCategoryRequest.description = createCategoryAJAXRequest.description;
        createCategoryRequest.keywords = createCategoryAJAXRequest.keywords;
        createCategoryRequest.tags = createCategoryAJAXRequest.tags;
        createCategoryRequest.fields = createCategoryAJAXRequest.fields;
        createCategoryRequest.imageURL = createCategoryAJAXRequest.imageURL;
        createCategoryRequest.requestBy = userInfo.username();
        createCategoryRequest.ownerId = createCategoryAJAXRequest.ownerId;
        createCategoryRequest.ownerRoles = createCategoryAJAXRequest.ownerRoles;
        createCategoryRequest.groupId = createCategoryAJAXRequest.groupId;
        createCategoryRequest.groupRoles = createCategoryAJAXRequest.groupRoles;
        createCategoryRequest.othersRoles = createCategoryAJAXRequest.othersRoles;
        /*if (!Strings.isNullOrEmpty(createCategoryAJAXRequest.parentId)) {
            return response(pageCategoryAccessService.auth(request.userInfo(), createCategoryAJAXRequest.parentId, "CREATE", () -> categoryWebService.create(createCategoryRequest)));
        }*/
        return response(pageCategoryWebService.create(createCategoryRequest));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public CategoryResponse update(@PathParam("id") String id, UpdateCategoryRequest ajaxRequest) throws IOException {
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest();
        updateCategoryRequest.parentId = ajaxRequest.parentId;
        updateCategoryRequest.displayName = ajaxRequest.displayName;
        updateCategoryRequest.description = ajaxRequest.description;
        updateCategoryRequest.displayOrder = ajaxRequest.displayOrder;
        updateCategoryRequest.keywords = ajaxRequest.keywords;
        updateCategoryRequest.tags = ajaxRequest.tags;
        updateCategoryRequest.fields = ajaxRequest.fields;
        updateCategoryRequest.imageURL = ajaxRequest.imageURL;
        updateCategoryRequest.requestBy = userInfo.username();
        updateCategoryRequest.ownerId = ajaxRequest.ownerId;
        updateCategoryRequest.ownerRoles = ajaxRequest.ownerRoles;
        updateCategoryRequest.groupId = ajaxRequest.groupId;
        updateCategoryRequest.groupRoles = ajaxRequest.groupRoles;
        updateCategoryRequest.othersRoles = ajaxRequest.othersRoles;
//        return pageCategoryAccessService.auth(request.userInfo(), id, "UPDATE",
//            () -> categoryWebService.update(id, updateCategoryRequest));
        return pageCategoryWebService.update(id, updateCategoryRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest();
        deleteCategoryRequest.ids = Lists.newArrayList(id);
        deleteCategoryRequest.requestBy = userInfo.username();
        /*pageCategoryAccessService.auth(request.userInfo(), id, "DELETE", () -> {
            categoryWebService.delete(deleteCategoryRequest);
            return null;
        });*/
        pageCategoryWebService.delete(deleteCategoryRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/delete")
    @POST
    public void delete(DeleteCategoryAJAXRequest ajaxRequest) {
        DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest();
        deleteCategoryRequest.ids = ajaxRequest.ids;
        deleteCategoryRequest.requestBy = userInfo.username();
        //deleteCategoryRequest.ids.forEach(id -> pageCategoryAccessService.auth(request.userInfo(), id, "DELETE", () -> null));
        pageCategoryWebService.delete(deleteCategoryRequest);
    }

    private CategoryAJAXResponse response(CategoryResponse response) {
        CategoryAJAXResponse ajaxResponse = new CategoryAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.parentId = response.parentId;
        ajaxResponse.displayName = response.displayName;
        ajaxResponse.keywords = response.keywords;
        ajaxResponse.displayOrder = response.displayOrder;
        ajaxResponse.description = response.description;
        ajaxResponse.imageURL = response.imageURL;
        ajaxResponse.tags = response.tags;
        ajaxResponse.fields = response.fields;
        ajaxResponse.status = response.status;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.updatedBy = response.updatedBy;
        ajaxResponse.ownerId = response.ownerId;
        ajaxResponse.ownerRoles = response.ownerRoles;
        ajaxResponse.groupId = response.groupId;
        ajaxResponse.groupRoles = response.groupRoles;
        ajaxResponse.othersRoles = response.othersRoles;
        return ajaxResponse;
    }
}
