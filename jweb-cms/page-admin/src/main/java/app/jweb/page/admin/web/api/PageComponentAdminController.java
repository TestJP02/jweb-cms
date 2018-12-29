package app.jweb.page.admin.web.api;

import app.jweb.page.api.PageComponentWebService;
import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.component.ComponentResponse;
import app.jweb.page.api.component.SavedComponentQuery;
import app.jweb.page.api.component.SavedComponentResponse;
import com.google.common.collect.Lists;
import app.jweb.page.admin.web.api.component.PageComponentAJAXResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/page/component")
public class PageComponentAdminController {
    @Inject
    PageSavedComponentWebService pageSavedComponentWebService;
    @Inject
    PageComponentWebService pageComponentWebService;

    @RolesAllowed("GET")
    @Path("/section")
    @GET
    public List<PageComponentAJAXResponse> sectionComponents() {
        List<PageComponentAJAXResponse> components = Lists.newArrayList();

        SavedComponentQuery savedComponentQuery = new SavedComponentQuery();
        components.addAll(pageSavedComponentWebService.find(savedComponentQuery).items.stream().map(this::componentAJAXView).collect(Collectors.toList()));
        components.addAll(pageComponentWebService.find().stream().map(this::componentAJAXView).collect(Collectors.toList()));
        return components;
    }

    @RolesAllowed("GET")
    @Path("/raw")
    @GET
    public List<PageComponentAJAXResponse> components() {
        List<PageComponentAJAXResponse> components = Lists.newArrayList();
        components.addAll(pageComponentWebService.find().stream().map(this::componentAJAXView).collect(Collectors.toList()));
        return components;
    }

    private PageComponentAJAXResponse componentAJAXView(SavedComponentResponse response) {
        PageComponentAJAXResponse ajaxResponse = new PageComponentAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.name = response.name;
        ajaxResponse.componentName = response.componentName;
        ajaxResponse.savedComponent = true;
        ajaxResponse.displayName = response.displayName;
        ajaxResponse.attributes = response.attributes;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.updatedBy = response.updatedBy;
        return ajaxResponse;
    }

    private PageComponentAJAXResponse componentAJAXView(ComponentResponse response) {
        PageComponentAJAXResponse ajaxResponse = new PageComponentAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.name = response.name;
        ajaxResponse.componentName = response.name;
        ajaxResponse.savedComponent = false;
        ajaxResponse.attributes = response.attributes;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.updatedBy = response.updatedBy;
        return ajaxResponse;
    }
}
