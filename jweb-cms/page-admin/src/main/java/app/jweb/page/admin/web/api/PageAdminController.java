package app.jweb.page.admin.web.api;

import app.jweb.page.admin.web.api.component.PageComponentAJAXView;
import app.jweb.page.admin.web.api.template.CreatePageAJAXRequest;
import app.jweb.page.admin.web.api.template.DeleteTemplateAJAXRequest;
import app.jweb.page.admin.web.api.template.PageAJAXResponse;
import app.jweb.page.admin.web.api.template.PageTemplateAJAXQuery;
import app.jweb.page.admin.web.api.template.PageTemplateSectionAJAXView;
import app.jweb.page.admin.web.api.template.UpdatePageAJAXRequest;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.DeletePageRequest;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.api.template.PageComponentView;
import app.jweb.page.api.template.PageSectionView;
import app.jweb.page.api.template.PageTemplateResponse;
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
public class PageAdminController {
    @Inject
    PageWebService pageWebService;
    @Inject
    PageTemplateWebService pageTemplateWebService;

    @Inject
    UserInfo userInfo;

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public PageAJAXResponse get(@PathParam("id") String id) {
        return response(pageWebService.get(id));
    }

    @RolesAllowed("GET")
    @GET
    public List<PageAJAXResponse> templates() {
        return pageWebService.find(new PageQuery()).map(this::response).items;
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<PageAJAXResponse> find(PageTemplateAJAXQuery query) {
        return pageWebService.find(query(query)).map(this::response);
    }

    @RolesAllowed("CREATE")
    @POST
    public PageAJAXResponse create(CreatePageAJAXRequest createPageAJAXRequest) {
        CreatePageRequest createPageRequest = new CreatePageRequest();
        createPageRequest.userId = userInfo.id();
        createPageRequest.path = createPageAJAXRequest.path;
        createPageRequest.title = createPageAJAXRequest.title;
        createPageRequest.tags = createPageAJAXRequest.tags;
        createPageRequest.description = createPageAJAXRequest.description;
        createPageRequest.sections = createPageAJAXRequest.sections;
        createPageRequest.requestBy = userInfo.username();
        createPageRequest.status = createPageAJAXRequest.status;
        return response(pageWebService.create(createPageRequest));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public PageAJAXResponse update(@PathParam("id") String id, UpdatePageAJAXRequest updatePageAJAXRequest) {
        UpdatePageRequest updatePageRequest = new UpdatePageRequest();
        updatePageRequest.path = updatePageAJAXRequest.path;
        updatePageRequest.categoryId = updatePageAJAXRequest.categoryId;
        updatePageRequest.title = updatePageAJAXRequest.title;
        updatePageRequest.description = updatePageAJAXRequest.description;
        updatePageRequest.tags = updatePageAJAXRequest.tags;
        updatePageRequest.keywords = updatePageAJAXRequest.keywords;
        updatePageRequest.sections = updatePageAJAXRequest.sections;
        updatePageRequest.requestBy = userInfo.username();
        updatePageRequest.status = updatePageAJAXRequest.status;
        return response(pageWebService.update(id, updatePageRequest));
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        DeletePageRequest deletePageRequest = new DeletePageRequest();
        deletePageRequest.ids = Lists.newArrayList(id);
        deletePageRequest.requestBy = userInfo.username();
        pageWebService.delete(deletePageRequest);
    }

    @RolesAllowed("DELETE")
    @Path("/batch-delete")
    @POST
    public void batchDelete(DeleteTemplateAJAXRequest deleteTemplateAJAXRequest) {
        DeletePageRequest deletePageRequest = new DeletePageRequest();
        deletePageRequest.ids = Lists.newArrayList(deleteTemplateAJAXRequest.ids);
        deletePageRequest.requestBy = userInfo.username();
        pageWebService.delete(deletePageRequest);
    }

    private PageQuery query(PageTemplateAJAXQuery ajaxQuery) {
        PageQuery query = new PageQuery();
        query.query = ajaxQuery.query;
        query.page = ajaxQuery.page;
        query.limit = ajaxQuery.limit;
        query.desc = ajaxQuery.desc;
        query.sortingField = ajaxQuery.sortingField;
        query.status = ajaxQuery.status;
        return query;
    }

    private PageAJAXResponse response(PageResponse pageResponse) {
        PageAJAXResponse response = new PageAJAXResponse();
        response.id = pageResponse.id;
        response.userId = pageResponse.userId;
        response.categoryId = pageResponse.categoryId;
        response.path = pageResponse.path;
        response.title = pageResponse.title;
        response.tags = pageResponse.tags;
        response.description = pageResponse.description;

        PageTemplateResponse pageTemplate = pageTemplateWebService.get(pageResponse.id);

        response.sections = pageTemplate.sections == null ? ImmutableList.of() : pageTemplate.sections.stream().map(this::pageTemplateSectionAJAXView).collect(Collectors.toList());
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

    private PageComponentAJAXView pageComponentAJAXView(PageComponentView pageComponentView) {
        PageComponentAJAXView pageComponentAJAXView = new PageComponentAJAXView();
        pageComponentAJAXView.id = pageComponentView.id;
        pageComponentAJAXView.name = pageComponentView.name;
        pageComponentAJAXView.attributes = pageComponentView.attributes;
        return pageComponentAJAXView;
    }
}
