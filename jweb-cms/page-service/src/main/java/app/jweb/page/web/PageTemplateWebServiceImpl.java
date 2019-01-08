package app.jweb.page.web;

import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.template.PageComponentView;
import app.jweb.page.api.template.PageSectionView;
import app.jweb.page.api.template.PageTemplateResponse;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.page.domain.PageTemplate;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.page.service.PageTemplateService;
import app.jweb.util.JSON;
import app.jweb.util.type.Types;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTemplateWebServiceImpl implements PageTemplateWebService {
    @Inject
    PageTemplateService pageTemplateService;
    @Inject
    PageSavedComponentService pageSavedComponentService;

    @Override
    public PageTemplateResponse get(String pageId) {
        PageTemplate pageTemplate = pageTemplateService.get(pageId);
        return response(pageTemplate);
    }

    private PageTemplateResponse response(PageTemplate template) {
        PageTemplateResponse response = new PageTemplateResponse();
        response.pageId = template.pageId;
        if (template.sections != null) {
            response.sections = JSON.fromJSON(template.sections, Types.generic(List.class, PageSectionView.class));
            response.components = Maps.newHashMap();
            components(response.sections, response.components);
        } else {
            response.sections = ImmutableList.of();
        }
        response.createdTime = template.createdTime;
        response.createdBy = template.createdBy;
        response.updatedTime = template.updatedTime;
        response.updatedBy = template.updatedBy;
        return response;
    }

    private void components(List<PageSectionView> sections, Map<String, PageComponentView> components) {
        for (PageSectionView section : sections) {
            if (section.components != null) {
                for (PageComponentView component : section.components) {
                    Optional<PageSavedComponent> pageSavedComponent = pageSavedComponentService.findById(component.id);
                    pageSavedComponent.ifPresent(savedComponent -> component.attributes = JSON.fromJSON(savedComponent.attributes, Map.class));
                    components.put(component.id, component);
                }
            }
            if (section.children != null) {
                components(section.children, components);
            }
        }
    }
}
