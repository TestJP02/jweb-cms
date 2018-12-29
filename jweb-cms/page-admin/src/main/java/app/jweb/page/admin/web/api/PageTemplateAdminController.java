package app.jweb.page.admin.web.api;

import app.jweb.page.admin.web.api.component.PageComponentAJAXView;
import app.jweb.page.admin.web.api.template.CreatePageTemplateAJAXRequest;
import app.jweb.page.admin.web.api.template.DeleteTemplateAJAXRequest;
import app.jweb.page.admin.web.api.template.PageTemplateAJAXQuery;
import app.jweb.page.admin.web.api.template.PageTemplateAJAXResponse;
import app.jweb.page.admin.web.api.template.PageTemplateSectionAJAXView;
import app.jweb.page.admin.web.api.template.UpdatePageTemplateAJAXRequest;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.component.PostComponentView;
import app.jweb.page.api.template.BatchDeletePageRequest;
import app.jweb.page.api.template.CreateTemplateRequest;
import app.jweb.page.api.template.TemplateQuery;
import app.jweb.page.api.template.TemplateResponse;
import app.jweb.page.api.template.TemplateSectionView;
import app.jweb.page.api.template.TemplateType;
import app.jweb.page.api.template.UpdateTemplateRequest;
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
    PageTemplateWebService pageTemplateWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public PageTemplateAJAXResponse get(@PathParam("id") String id) {
        return response(pageTemplateWebService.get(id));
    }

    @RolesAllowed("GET")
    @GET
    public List<PageTemplateAJAXResponse> templates() {
        return pageTemplateWebService.find(new TemplateQuery()).map(this::response).items;
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<PageTemplateAJAXResponse> find(PageTemplateAJAXQuery query) {
        return pageTemplateWebService.find(query(query)).map(this::response);
    }

    @RolesAllowed("CREATE")
    @POST
    public PageTemplateAJAXResponse create(CreatePageTemplateAJAXRequest createPageTemplateAJAXRequest) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.userId = userInfo.id();
        createTemplateRequest.path = createPageTemplateAJAXRequest.path;
        createTemplateRequest.templatePath = createPageTemplateAJAXRequest.templatePath;
        createTemplateRequest.title = createPageTemplateAJAXRequest.title;
        createTemplateRequest.tags = createPageTemplateAJAXRequest.tags;
        createTemplateRequest.description = createPageTemplateAJAXRequest.description;
        createTemplateRequest.sections = createPageTemplateAJAXRequest.sections;
        createTemplateRequest.requestBy = userInfo.username();
        createTemplateRequest.type = TemplateType.CUSTOMIZED;
        return response(pageTemplateWebService.create(createTemplateRequest));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public PageTemplateAJAXResponse update(@PathParam("id") String id, UpdatePageTemplateAJAXRequest updatePageTemplateAJAXRequest) {
        UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest();
        updateTemplateRequest.path = updatePageTemplateAJAXRequest.path;
        updateTemplateRequest.templatePath = updatePageTemplateAJAXRequest.templatePath;
        updateTemplateRequest.title = updatePageTemplateAJAXRequest.title;
        updateTemplateRequest.description = updatePageTemplateAJAXRequest.description;
        updateTemplateRequest.tags = updatePageTemplateAJAXRequest.tags;
        updateTemplateRequest.sections = updatePageTemplateAJAXRequest.sections;
        updateTemplateRequest.type = TemplateType.CUSTOMIZED;
        updateTemplateRequest.requestBy = userInfo.username();
        return response(pageTemplateWebService.update(id, updateTemplateRequest));
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        BatchDeletePageRequest batchDeletePageRequest = new BatchDeletePageRequest();
        batchDeletePageRequest.ids = Lists.newArrayList(id);
        batchDeletePageRequest.requestBy = userInfo.username();
        pageTemplateWebService.batchDelete(batchDeletePageRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void batchDelete(DeleteTemplateAJAXRequest deleteTemplateAJAXRequest) {
        BatchDeletePageRequest batchDeletePageRequest = new BatchDeletePageRequest();
        batchDeletePageRequest.ids = Lists.newArrayList(deleteTemplateAJAXRequest.ids);
        batchDeletePageRequest.requestBy = userInfo.username();
        pageTemplateWebService.batchDelete(batchDeletePageRequest);
    }

    private TemplateQuery query(PageTemplateAJAXQuery ajaxQuery) {
        TemplateQuery query = new TemplateQuery();
        query.templatePath = ajaxQuery.path;
        query.page = ajaxQuery.page;
        query.limit = ajaxQuery.limit;
        query.desc = ajaxQuery.desc;
        query.sortingField = ajaxQuery.sortingField;
        query.status = ajaxQuery.status;
        return query;
    }

    private PageTemplateAJAXResponse response(TemplateResponse templateResponse) {
        PageTemplateAJAXResponse response = new PageTemplateAJAXResponse();
        response.id = templateResponse.id;
        response.path = templateResponse.path;
        response.userId = templateResponse.userId;
        response.templatePath = templateResponse.templatePath;
        response.type = templateResponse.type;
        response.title = templateResponse.title;
        response.tags = templateResponse.tags;
        response.description = templateResponse.description;
        response.sections = templateResponse.sections == null ? ImmutableList.of() : templateResponse.sections.stream().map(this::pageTemplateSectionAJAXView).collect(Collectors.toList());
        response.status = templateResponse.status;
        response.createdTime = templateResponse.createdTime;
        response.updatedTime = templateResponse.updatedTime;
        response.createdBy = templateResponse.createdBy;
        response.updatedBy = templateResponse.updatedBy;
        return response;
    }

    private PageTemplateSectionAJAXView pageTemplateSectionAJAXView(TemplateSectionView templateSectionView) {
        PageTemplateSectionAJAXView pageTemplateSectionAJAXView = new PageTemplateSectionAJAXView();
        pageTemplateSectionAJAXView.id = templateSectionView.id;
        pageTemplateSectionAJAXView.name = templateSectionView.name;
        pageTemplateSectionAJAXView.widths = templateSectionView.widths;
        pageTemplateSectionAJAXView.children = templateSectionView.children == null ? ImmutableList.of() : templateSectionView.children.stream().map(this::pageTemplateSectionAJAXView).collect(Collectors.toList());
        pageTemplateSectionAJAXView.components = templateSectionView.components == null ? ImmutableList.of() : templateSectionView.components.stream().map(this::pageComponentAJAXView).collect(Collectors.toList());
        pageTemplateSectionAJAXView.wrapper = templateSectionView.wrapper;
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
