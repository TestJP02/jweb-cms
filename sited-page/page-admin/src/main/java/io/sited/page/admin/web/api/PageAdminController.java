package io.sited.page.admin.web.api;


import com.google.common.collect.Lists;
import io.sited.page.admin.PageAdminOptions;
import io.sited.page.admin.web.api.page.DeletePageAJAXRequest;
import io.sited.page.admin.web.api.page.PageAdminQuery;
import io.sited.page.admin.web.api.page.PageAdminRequest;
import io.sited.page.admin.web.api.page.PageAdminResponse;
import io.sited.page.admin.web.api.page.SiteURLResponse;
import io.sited.page.api.PageContentWebService;
import io.sited.page.api.PageDraftWebService;
import io.sited.page.api.PageStatisticsWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.draft.CreateDraftRequest;
import io.sited.page.api.draft.DraftQuery;
import io.sited.page.api.draft.DraftResponse;
import io.sited.page.api.draft.UpdateDraftRequest;
import io.sited.page.api.page.DeletePageRequest;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.page.RevertDeletePageRequest;
import io.sited.page.api.statistics.PageStatisticsResponse;
import io.sited.util.collection.QueryResponse;
import io.sited.web.ClientInfo;
import io.sited.web.UserInfo;

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
@Path("/admin/api/page")
public class PageAdminController {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageAdminOptions pageAdminOptions;
    @Inject
    PageDraftWebService pageDraftWebService;
    @Inject
    PageStatisticsWebService pageStatisticsWebService;
    @Inject
    PageContentWebService pageContentWebService;

    @Inject
    UserInfo userInfo;
    @Inject
    ClientInfo clientInfo;

    @RolesAllowed("GET")
    @Path("/{pageId}/draft")
    @GET
    public PageAdminResponse pageDraft(@PathParam("pageId") String pageId) {
        PageResponse page = pageWebService.get(pageId);
        Optional<DraftResponse> draft = pageDraftWebService.findByPageId(page.id);
        return draft.map(this::response).orElseGet(() -> draft(page));
    }

    @RolesAllowed("GET")
    @Path("/draft/{id}")
    @GET
    public PageAdminResponse draft(@PathParam("id") String id) {
        DraftResponse draft = pageDraftWebService.get(id);
        return response(draft);
    }

    private PageAdminResponse draft(PageResponse page) {
        PageAdminResponse response = new PageAdminResponse();
        response.title = page.title;
        response.description = page.description;
        response.version = page.version + 1;
        response.categoryId = page.categoryId;
        response.pageId = page.id;
        response.userId = page.userId;
        response.path = page.path;
        response.templatePath = page.templatePath;
        response.tags = page.tags;
        response.imageURL = page.imageURL;
        response.keywords = page.keywords;
        response.content = pageContentWebService.getByPageId(page.id).content;
        response.fields = page.fields;
        response.status = PageStatus.DRAFT;
        response.createdBy = page.createdBy;
        response.createdTime = page.createdTime;
        response.updatedBy = page.updatedBy;
        response.updatedTime = page.updatedTime;
        return response;
    }

    @RolesAllowed("GET")
    @Path("/draft")
    @GET
    public PageAdminResponse defaultDraft() {
        PageAdminResponse response = new PageAdminResponse();
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
    public QueryResponse<PageAdminResponse> find(PageAdminQuery query) throws IOException {
        if (query.status == PageStatus.DRAFT || query.status == PageStatus.AUDITING) {
            DraftQuery pageQuery = new DraftQuery();
            pageQuery.query = query.query;
            pageQuery.categoryId = query.categoryId;
            pageQuery.limit = query.limit;
            pageQuery.page = query.page;
            pageQuery.status = query.status;
            pageQuery.desc = query.desc;
            pageQuery.sortingField = query.sortingField;
            return pageDraftWebService.find(pageQuery).map(this::response);
        } else if (query.status == null) {
            PageQuery pageQuery = new PageQuery();
            pageQuery.query = query.query;
            pageQuery.categoryId = query.categoryId;
            pageQuery.limit = query.limit;
            pageQuery.page = query.page;
            pageQuery.status = query.status;
            pageQuery.desc = query.desc;
            pageQuery.sortingField = query.sortingField;
            QueryResponse<PageAdminResponse> response = pageWebService.find(pageQuery).map(this::response);

            if (query.page == null || query.page == 1) {
                DraftQuery draftQuery = new DraftQuery();
                draftQuery.query = query.query;
                draftQuery.categoryId = query.categoryId;
                draftQuery.limit = 5;
                draftQuery.page = 1;
                draftQuery.status = query.status;
                draftQuery.desc = query.desc;
                draftQuery.sortingField = query.sortingField;
                QueryResponse<PageAdminResponse> drafts = pageDraftWebService.find(draftQuery).map(this::response);

                List<PageAdminResponse> items = Lists.newArrayList(drafts.items);
                items.addAll(response.items);
                response.items = items;
            }
            return response;
        } else {
            PageQuery pageQuery = new PageQuery();
            pageQuery.query = query.query;
            pageQuery.categoryId = query.categoryId;
            pageQuery.limit = query.limit;
            pageQuery.page = query.page;
            pageQuery.status = query.status;
            pageQuery.desc = query.desc;
            pageQuery.sortingField = query.sortingField;
            return pageWebService.find(pageQuery).map(this::response);
        }
    }

    @RolesAllowed("CREATE")
    @Path("/draft")
    @POST
    public PageAdminResponse save(PageAdminRequest draft) {
        if (draft.id == null) {
            //pageCategoryAccessService.auth(request.userInfo(), draft.categoryId, "CREATE", () -> true);
            DraftResponse draftResponse = pageDraftWebService.create(createPageDraftRequest(draft, userInfo));
            return response(draftResponse);
        } else {
            //DraftResponse draftResponse = draftWebService.get(draft.id);
            //pageCategoryAccessService.auth(request.userInfo(), draftResponse.categoryId, "UPDATE", () -> true);
            DraftResponse pageDraftResponse = pageDraftWebService.update(draft.id, updatePageDraftRequest(draft, userInfo.username()));
            return response(pageDraftResponse);
        }
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void delete(DeletePageAJAXRequest deletePageAJAXRequest) {
        DeletePageRequest deletePageRequest = new DeletePageRequest();
        deletePageRequest.pages = deletePageAJAXRequest.pages;
        deletePageRequest.requestBy = userInfo.username();
        pageWebService.batchDelete(deletePageRequest);
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/publish")
    @GET
    public void publish(@PathParam("id") String id) {
        pageDraftWebService.publish(id, userInfo.username());
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/revert")
    @GET
    public void revert(@PathParam("id") String id) {
        RevertDeletePageRequest revertDeletePageRequest = new RevertDeletePageRequest();
        revertDeletePageRequest.ids = Lists.newArrayList(id);
        revertDeletePageRequest.requestBy = userInfo.username();
        //PageResponse pageResponse = pageWebService.get(id);
        //pageCategoryAccessService.auth(request.userInfo(), pageResponse.categoryId, "UPDATE", () -> true);
        pageWebService.revertDelete(revertDeletePageRequest);
    }


    @RolesAllowed("LIST")
    @Path("/site/url")
    @GET
    public SiteURLResponse siteURL() {
        SiteURLResponse siteURLResponse = new SiteURLResponse();
        siteURLResponse.siteURL = pageAdminOptions.siteURL;
        return siteURLResponse;
    }

    private PageAdminResponse response(DraftResponse draft) {
        PageAdminResponse response = new PageAdminResponse();
        response.id = draft.id;
        response.pageId = draft.pageId;
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
        return response;
    }

    private PageAdminResponse response(PageResponse page) {
        PageAdminResponse response = new PageAdminResponse();
        response.id = page.id;
        response.pageId = page.id;
        response.categoryId = page.categoryId;
        response.path = page.path;
        response.templatePath = page.templatePath;
        response.version = page.version;
        response.userId = page.userId;
        response.title = page.title;

        Optional<PageStatisticsResponse> pageStatisticsResponse = pageStatisticsWebService.findById(page.id);
        if (pageStatisticsResponse.isPresent()) {
            response.totalCommented = pageStatisticsResponse.get().totalCommented;
            response.totalVisited = pageStatisticsResponse.get().totalVisited;
        } else {
            response.totalCommented = 0;
            response.totalVisited = 0;
        }
        response.description = page.description;
        response.imageURL = page.imageURL;
        response.keywords = page.keywords;
        response.tags = page.tags;
        response.fields = page.fields;
        response.status = page.status;
        response.createdTime = page.createdTime;
        response.createdBy = page.createdBy;
        response.updatedTime = page.updatedTime;
        response.updatedBy = page.updatedBy;
        return response;
    }

    private CreateDraftRequest createPageDraftRequest(PageAdminRequest request, UserInfo userInfo) {
        CreateDraftRequest createDraftRequest = new CreateDraftRequest();
        createDraftRequest.userId = userInfo.id();
        createDraftRequest.categoryId = request.categoryId;
        createDraftRequest.pageId = request.pageId;
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
        return createDraftRequest;
    }

    private UpdateDraftRequest updatePageDraftRequest(PageAdminRequest request, String requestBy) {
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
        return updateDraftRequest;
    }
}
