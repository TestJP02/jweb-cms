package app.jweb.page.admin.web.api;

import app.jweb.page.admin.web.api.component.PageComponentAJAXView;
import app.jweb.page.admin.web.api.template.CreatePageTemplateAJAXRequest;
import app.jweb.page.admin.web.api.template.DeleteTemplateAJAXRequest;
import app.jweb.page.admin.web.api.template.PageTemplateAJAXQuery;
import app.jweb.page.admin.web.api.template.PageTemplateAJAXResponse;
import app.jweb.page.admin.web.api.template.PageTemplateSectionAJAXView;
import app.jweb.page.admin.web.api.template.UpdatePageTemplateAJAXRequest;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.component.PostComponentView;
import app.jweb.page.api.page.BatchDeletePageRequest;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PageSectionView;
import app.jweb.page.api.page.PageType;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.UserInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/page/template")
public class PageTemplateAdminController {
    @Inject
    PageWebService pageWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public PageTemplateAJAXResponse get(@PathParam("id") String id) {
        return response(pageWebService.get(id));
    }

    @RolesAllowed("GET")
    @GET
    public List<PageTemplateAJAXResponse> templates() {
        return pageWebService.find(new PageQuery()).map(this::response).items;
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<PageTemplateAJAXResponse> find(PageTemplateAJAXQuery query) {
        return pageWebService.find(query(query)).map(this::response);
    }

    @RolesAllowed("CREATE")
    @POST
    public PageTemplateAJAXResponse create(CreatePageTemplateAJAXRequest createPageTemplateAJAXRequest) {
        CreatePageRequest createPageRequest = new CreatePageRequest();
        createPageRequest.userId = userInfo.id();
        createPageRequest.path = createPageTemplateAJAXRequest.path;
        createPageRequest.templatePath = createPageTemplateAJAXRequest.templatePath;
        createPageRequest.title = createPageTemplateAJAXRequest.title;
        createPageRequest.tags = createPageTemplateAJAXRequest.tags;
        createPageRequest.description = createPageTemplateAJAXRequest.description;
        createPageRequest.sections = createPageTemplateAJAXRequest.sections;
        createPageRequest.requestBy = userInfo.username();
        createPageRequest.type = PageType.CUSTOMIZED;
        return response(pageWebService.create(createPageRequest));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public PageTemplateAJAXResponse update(@PathParam("id") String id, UpdatePageTemplateAJAXRequest updatePageTemplateAJAXRequest) {
        UpdatePageRequest updatePageRequest = new UpdatePageRequest();
        updatePageRequest.path = updatePageTemplateAJAXRequest.path;
        updatePageRequest.templatePath = updatePageTemplateAJAXRequest.templatePath;
        updatePageRequest.title = updatePageTemplateAJAXRequest.title;
        updatePageRequest.description = updatePageTemplateAJAXRequest.description;
        updatePageRequest.tags = updatePageTemplateAJAXRequest.tags;
        updatePageRequest.sections = updatePageTemplateAJAXRequest.sections;
        updatePageRequest.type = PageType.CUSTOMIZED;
        updatePageRequest.requestBy = userInfo.username();
        return response(pageWebService.update(id, updatePageRequest));
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        BatchDeletePageRequest batchDeletePageRequest = new BatchDeletePageRequest();
        batchDeletePageRequest.ids = Lists.newArrayList(id);
        batchDeletePageRequest.requestBy = userInfo.username();
        pageWebService.batchDelete(batchDeletePageRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void batchDelete(DeleteTemplateAJAXRequest deleteTemplateAJAXRequest) {
        BatchDeletePageRequest batchDeletePageRequest = new BatchDeletePageRequest();
        batchDeletePageRequest.ids = Lists.newArrayList(deleteTemplateAJAXRequest.ids);
        batchDeletePageRequest.requestBy = userInfo.username();
        pageWebService.batchDelete(batchDeletePageRequest);
    }

    private PageQuery query(PageTemplateAJAXQuery ajaxQuery) {
        PageQuery query = new PageQuery();
        query.templatePath = ajaxQuery.path;
        query.page = ajaxQuery.page;
        query.limit = ajaxQuery.limit;
        query.desc = ajaxQuery.desc;
        query.sortingField = ajaxQuery.sortingField;
        query.status = ajaxQuery.status;
        return query;
    }

    private PageTemplateAJAXResponse response(PageResponse pageResponse) {
        PageTemplateAJAXResponse response = new PageTemplateAJAXResponse();
        response.id = pageResponse.id;
        response.path = pageResponse.path;
        response.userId = pageResponse.userId;
        response.templatePath = pageResponse.templatePath;
        response.type = pageResponse.type;
        response.title = pageResponse.title;
        response.tags = pageResponse.tags;
        response.description = pageResponse.description;
        response.sections = pageResponse.sections == null ? ImmutableList.of() : pageResponse.sections.stream().map(this::pageTemplateSectionAJAXView).collect(Collectors.toList());
        response.status = pageResponse.status;
        response.createdTime = pageResponse.createdTime;
        response.updatedTime = pageResponse.updatedTime;
        response.createdBy = pageResponse.createdBy;
        response.updatedBy = pageResponse.updatedBy;
        return response;
    }

    private PageTemplateSectionAJAXView pageTemplateSectionAJAXView(PageSectionView pageSectionView) {
        PageTemplateSectionAJAXView pageTemplateSectionAJAXView = new PageTemplateSectionAJAXView();
        pageTemplateSectionAJAXView.id = pageSectionView.id;
        pageTemplateSectionAJAXView.name = pageSectionView.name;
        pageTemplateSectionAJAXView.widths = pageSectionView.widths;
        pageTemplateSectionAJAXView.children = pageSectionView.children == null ? ImmutableList.of() : pageSectionView.children.stream().map(this::pageTemplateSectionAJAXView).collect(Collectors.toList());
        pageTemplateSectionAJAXView.components = pageSectionView.components == null ? ImmutableList.of() : pageSectionView.components.stream().map(this::pageComponentAJAXView).collect(Collectors.toList());
        pageTemplateSectionAJAXView.wrapper = pageSectionView.wrapper;
        return pageTemplateSectionAJAXView;
    }

    private PageComponentAJAXView pageComponentAJAXView(PostComponentView postComponentView) {
        PageComponentAJAXView pageComponentAJAXView = new PageComponentAJAXView();
        pageComponentAJAXView.id = postComponentView.id;
        pageComponentAJAXView.name = postComponentView.name;
        pageComponentAJAXView.attributes = postComponentView.attributes;
        return pageComponentAJAXView;
    }
}
