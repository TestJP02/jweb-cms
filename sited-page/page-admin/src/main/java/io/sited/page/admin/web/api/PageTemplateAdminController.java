package io.sited.page.admin.web.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.page.admin.web.api.page.PageComponentAJAXView;
import io.sited.page.admin.web.api.template.CreatePageTemplateAJAXRequest;
import io.sited.page.admin.web.api.template.DeleteTemplateAJAXRequest;
import io.sited.page.admin.web.api.template.PageTemplateAJAXQuery;
import io.sited.page.admin.web.api.template.PageTemplateAJAXResponse;
import io.sited.page.admin.web.api.template.PageTemplateSectionAJAXView;
import io.sited.page.admin.web.api.template.UpdatePageTemplateAJAXRequest;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.page.PageComponentView;
import io.sited.page.api.template.BatchDeletePageRequest;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateQuery;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.template.TemplateSectionView;
import io.sited.page.api.template.UpdateTemplateRequest;
import io.sited.util.collection.QueryResponse;
import io.sited.web.UserInfo;

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
        CreateTemplateRequest pageTemplateView = new CreateTemplateRequest();
        pageTemplateView.path = createPageTemplateAJAXRequest.path;
        pageTemplateView.displayName = createPageTemplateAJAXRequest.displayName;
        pageTemplateView.sections = createPageTemplateAJAXRequest.sections;
        pageTemplateView.requestBy = userInfo.username();
        return response(pageTemplateWebService.create(pageTemplateView));
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public PageTemplateAJAXResponse update(@PathParam("id") String id, UpdatePageTemplateAJAXRequest updatePageTemplateAJAXRequest) {
        UpdateTemplateRequest updateRequest = new UpdateTemplateRequest();
        updateRequest.displayName = updatePageTemplateAJAXRequest.displayName;
        updateRequest.sections = updatePageTemplateAJAXRequest.sections;
        updateRequest.requestBy = userInfo.username();
        return response(pageTemplateWebService.update(id, updateRequest));
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
        query.path = ajaxQuery.path;
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
        response.readOnly = templateResponse.readOnly;
        response.displayName = templateResponse.displayName;
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

    private PageComponentAJAXView pageComponentAJAXView(PageComponentView pageComponentView) {
        PageComponentAJAXView pageComponentAJAXView = new PageComponentAJAXView();
        pageComponentAJAXView.id = pageComponentView.id;
        pageComponentAJAXView.name = pageComponentView.name;
        pageComponentAJAXView.attributes = pageComponentView.attributes;
        return pageComponentAJAXView;
    }
}
