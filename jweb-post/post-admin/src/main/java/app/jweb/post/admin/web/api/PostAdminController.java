package app.jweb.post.admin.web.api;


import app.jweb.post.admin.PostAdminOptions;
import app.jweb.post.admin.web.api.post.DeletePostAJAXRequest;
import app.jweb.post.admin.web.api.post.PostAdminQuery;
import app.jweb.post.admin.web.api.post.PostAdminRequest;
import app.jweb.post.admin.web.api.post.PostAdminResponse;
import app.jweb.post.admin.web.api.post.SiteURLResponse;
import app.jweb.post.api.PostContentWebService;
import app.jweb.post.api.PostDraftWebService;
import app.jweb.post.api.PostStatisticsWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftQuery;
import app.jweb.post.api.draft.DraftResponse;
import app.jweb.post.api.draft.UpdateDraftRequest;
import app.jweb.post.api.post.DeletePostRequest;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.post.RevertDeletePostRequest;
import app.jweb.post.api.statistics.PostStatisticsResponse;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.ClientInfo;
import app.jweb.web.UserInfo;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/admin/api/post")
public class PostAdminController {
    @Inject
    PostWebService postWebService;
    @Inject
    PostAdminOptions postAdminOptions;
    @Inject
    PostDraftWebService postDraftWebService;
    @Inject
    PostStatisticsWebService postStatisticsWebService;
    @Inject
    PostContentWebService postContentWebService;

    @Inject
    UserInfo userInfo;
    @Inject
    ClientInfo clientInfo;

    @RolesAllowed("GET")
    @Path("/{postId}/draft")
    @GET
    public PostAdminResponse postDraft(@PathParam("postId") String postId) {
        PostResponse post = postWebService.get(postId);
        Optional<DraftResponse> draft = postDraftWebService.findByPostId(post.id);
        return draft.map(this::response).orElseGet(() -> draft(post));
    }

    @RolesAllowed("GET")
    @Path("/draft/{id}")
    @GET
    public PostAdminResponse draft(@PathParam("id") String id) {
        DraftResponse draft = postDraftWebService.get(id);
        return response(draft);
    }

    private PostAdminResponse draft(PostResponse post) {
        PostAdminResponse response = new PostAdminResponse();
        response.title = post.title;
        response.description = post.description;
        response.version = post.version + 1;
        response.categoryId = post.categoryId;
        response.postId = post.id;
        response.userId = post.userId;
        response.path = post.path;
        response.templatePath = post.templatePath;
        response.tags = post.tags;
        response.imageURL = post.imageURL;
        response.keywords = post.keywords;
        response.content = postContentWebService.getByPostId(post.id).content;
        response.fields = post.fields;
        response.status = PostStatus.DRAFT;
        response.createdBy = post.createdBy;
        response.createdTime = post.createdTime;
        response.updatedBy = post.updatedBy;
        response.updatedTime = post.updatedTime;
        return response;
    }

    @RolesAllowed("GET")
    @Path("/draft")
    @GET
    public PostAdminResponse defaultDraft() {
        PostAdminResponse response = new PostAdminResponse();
        response.version = 1;
        response.templatePath = "template/page.html";
        response.userId = userInfo.id();
        response.tags = Lists.newArrayList();
        response.keywords = Lists.newArrayList();
        response.content = "";
        return response;
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public QueryResponse<PostAdminResponse> find(PostAdminQuery query) throws IOException {
        if (query.status == PostStatus.DRAFT || query.status == PostStatus.AUDITING) {
            DraftQuery postQuery = new DraftQuery();
            postQuery.query = query.query;
            postQuery.categoryId = query.categoryId;
            postQuery.limit = query.limit;
            postQuery.page = query.page;
            postQuery.status = query.status;
            postQuery.desc = query.desc;
            postQuery.sortingField = query.sortingField;
            return postDraftWebService.find(postQuery).map(this::response);
        } else if (query.status == null) {
            PostQuery postQuery = new PostQuery();
            postQuery.query = query.query;
            postQuery.categoryId = query.categoryId;
            postQuery.limit = query.limit;
            postQuery.page = query.page;
            postQuery.status = query.status;
            postQuery.desc = query.desc;
            postQuery.sortingField = query.sortingField;
            QueryResponse<PostAdminResponse> response = postWebService.find(postQuery).map(this::response);

            if (query.page == null || query.page == 1) {
                DraftQuery draftQuery = new DraftQuery();
                draftQuery.query = query.query;
                draftQuery.categoryId = query.categoryId;
                draftQuery.limit = 5;
                draftQuery.page = 1;
                draftQuery.status = query.status;
                draftQuery.desc = query.desc;
                draftQuery.sortingField = query.sortingField;
                QueryResponse<PostAdminResponse> drafts = postDraftWebService.find(draftQuery).map(this::response);

                List<PostAdminResponse> items = Lists.newArrayList(drafts.items);
                items.addAll(response.items);
                response.items = items;
            }
            return response;
        } else {
            PostQuery postQuery = new PostQuery();
            postQuery.query = query.query;
            postQuery.categoryId = query.categoryId;
            postQuery.limit = query.limit;
            postQuery.page = query.page;
            postQuery.status = query.status;
            postQuery.desc = query.desc;
            postQuery.sortingField = query.sortingField;
            return postWebService.find(postQuery).map(this::response);
        }
    }

    @RolesAllowed("CREATE")
    @Path("/draft")
    @POST
    public PostAdminResponse save(PostAdminRequest draft) {
        if (draft.id == null) {
            //postCategoryAccessService.auth(request.userInfo(), draft.categoryId, "CREATE", () -> true);
            DraftResponse draftResponse = postDraftWebService.create(createPostDraftRequest(draft, userInfo));
            return response(draftResponse);
        } else {
            //DraftResponse draftResponse = draftWebService.get(draft.id);
            //postCategoryAccessService.auth(request.userInfo(), draftResponse.categoryId, "UPDATE", () -> true);
            DraftResponse postDraftResponse = postDraftWebService.update(draft.id, updatePostDraftRequest(draft, userInfo.username()));
            return response(postDraftResponse);
        }
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void delete(DeletePostAJAXRequest deletePostAJAXRequest) {
        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.posts = deletePostAJAXRequest.posts;
        deletePostRequest.requestBy = userInfo.username();
        postWebService.batchDelete(deletePostRequest);
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/publish")
    @GET
    public void publish(@PathParam("id") String id) {
        postDraftWebService.publish(id, userInfo.username());
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/revert")
    @GET
    public void revert(@PathParam("id") String id) {
        RevertDeletePostRequest revertDeletePostRequest = new RevertDeletePostRequest();
        revertDeletePostRequest.ids = Lists.newArrayList(id);
        revertDeletePostRequest.requestBy = userInfo.username();
        //PostResponse postResponse = postWebService.get(id);
        //postCategoryAccessService.auth(request.userInfo(), postResponse.categoryId, "UPDATE", () -> true);
        postWebService.revertDelete(revertDeletePostRequest);
    }


    @RolesAllowed("LIST")
    @Path("/site/url")
    @GET
    public SiteURLResponse siteURL() {
        SiteURLResponse siteURLResponse = new SiteURLResponse();
        siteURLResponse.siteURL = postAdminOptions.siteURL;
        return siteURLResponse;
    }

    private PostAdminResponse response(DraftResponse draft) {
        PostAdminResponse response = new PostAdminResponse();
        response.id = draft.id;
        response.postId = draft.postId;
        response.categoryId = draft.categoryId;
        response.path = draft.path;
        response.templatePath = draft.templatePath;
        response.version = draft.version;
        response.userId = draft.userId;
        response.title = draft.title;
        response.description = draft.description;
        response.totalVisited = 0;
        response.totalCommented = 0;
        response.imageURL = draft.imageURL;
        response.keywords = draft.keywords;
        response.tags = draft.tags;
        response.fields = draft.fields;
        response.status = draft.status;
        response.createdTime = draft.createdTime;
        response.createdBy = draft.createdBy;
        response.updatedTime = draft.updatedTime;
        response.updatedBy = draft.updatedBy;
        response.content = draft.content;
        response.topFixed = draft.topFixed;
        return response;
    }

    private PostAdminResponse response(PostResponse post) {
        PostAdminResponse response = new PostAdminResponse();
        response.id = post.id;
        response.postId = post.id;
        response.categoryId = post.categoryId;
        response.path = post.path;
        response.templatePath = post.templatePath;
        response.version = post.version;
        response.userId = post.userId;
        response.title = post.title;

        Optional<PostStatisticsResponse> postStatisticsResponse = postStatisticsWebService.findById(post.id);
        if (postStatisticsResponse.isPresent()) {
            response.totalCommented = postStatisticsResponse.get().totalCommented;
            response.totalVisited = postStatisticsResponse.get().totalVisited;
        } else {
            response.totalCommented = 0;
            response.totalVisited = 0;
        }
        response.description = post.description;
        response.imageURL = post.imageURL;
        response.keywords = post.keywords;
        response.tags = post.tags;
        response.fields = post.fields;
        response.status = post.status;
        response.topFixed = post.topFixed;
        response.createdTime = post.createdTime;
        response.createdBy = post.createdBy;
        response.updatedTime = post.updatedTime;
        response.updatedBy = post.updatedBy;
        return response;
    }

    private CreateDraftRequest createPostDraftRequest(PostAdminRequest request, UserInfo userInfo) {
        CreateDraftRequest createDraftRequest = new CreateDraftRequest();
        createDraftRequest.userId = userInfo.id();
        createDraftRequest.categoryId = request.categoryId;
        createDraftRequest.postId = request.postId;
        createDraftRequest.path = request.path;
        createDraftRequest.version = request.version;
        createDraftRequest.templatePath = request.templatePath;
        createDraftRequest.title = request.title;
        createDraftRequest.description = request.description;
        createDraftRequest.fields = request.fields;
        createDraftRequest.tags = request.tags;
        createDraftRequest.keywords = request.keywords;
        createDraftRequest.content = request.content;
        createDraftRequest.imageURL = request.imageURL;
        createDraftRequest.requestBy = userInfo.username();
        createDraftRequest.topFixed = request.topFixed;
        return createDraftRequest;
    }

    private UpdateDraftRequest updatePostDraftRequest(PostAdminRequest request, String requestBy) {
        UpdateDraftRequest updateDraftRequest = new UpdateDraftRequest();
        updateDraftRequest.categoryId = request.categoryId;
        updateDraftRequest.path = request.path;
        updateDraftRequest.templatePath = request.templatePath;
        updateDraftRequest.title = request.title;
        updateDraftRequest.description = request.description;
        updateDraftRequest.tags = request.tags;
        updateDraftRequest.keywords = request.keywords;
        updateDraftRequest.fields = request.fields;
        updateDraftRequest.content = request.content;
        updateDraftRequest.imageURL = request.imageURL;
        updateDraftRequest.requestBy = requestBy;
        updateDraftRequest.topFixed = request.topFixed;
        return updateDraftRequest;
    }
}
