package io.sited.page.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.api.PageTemplateWebService;
import io.sited.page.api.page.PageComponentView;
import io.sited.page.api.template.BatchCreateTemplateRequest;
import io.sited.page.api.template.BatchDeletePageRequest;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateQuery;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.template.TemplateSectionView;
import io.sited.page.api.template.UpdateTemplateRequest;
import io.sited.page.domain.PageSavedComponent;
import io.sited.page.domain.PageTemplate;
import io.sited.page.service.PageComponentService;
import io.sited.page.service.PageSavedComponentService;
import io.sited.page.service.PageTemplateService;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;
import io.sited.util.type.Types;

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
    @Inject
    PageComponentService pageComponentService;

    @Override
    public TemplateResponse get(String id) {
        return response(pageTemplateService.get(id));
    }

    @Override
    public Optional<TemplateResponse> findByTemplatePath(String path) {
        Optional<PageTemplate> pageTemplate = pageTemplateService.findByPath(path);
        return pageTemplate.map(this::response);
    }

    @Override
    public QueryResponse<TemplateResponse> find(TemplateQuery query) {
        return pageTemplateService.find(query).map(this::response);
    }

    @Override
    public TemplateResponse create(CreateTemplateRequest request) {
        return response(pageTemplateService.create(request));
    }

    @Override
    public void batchCreate(BatchCreateTemplateRequest request) {
        pageTemplateService.batchCreate(request);
    }

    @Override
    public TemplateResponse update(String id, UpdateTemplateRequest request) {
        return response(pageTemplateService.update(id, request));
    }

    @Override
    public void batchDelete(BatchDeletePageRequest request) {
        request.ids.forEach(id -> pageTemplateService.delete(id, request.requestBy));
    }

    private TemplateResponse response(PageTemplate template) {
        TemplateResponse response = new TemplateResponse();
        response.id = template.id;
        response.path = template.path;
        response.displayName = template.displayName;
        response.type = template.type;
        if (template.sections != null) {
            response.sections = JSON.fromJSON(template.sections, Types.generic(List.class, TemplateSectionView.class));
            response.components = Maps.newHashMap();
            components(response.sections, response.components);
        } else {
            response.sections = ImmutableList.of();
        }
        response.status = template.status;
        response.createdTime = template.createdTime;
        response.createdBy = template.createdBy;
        response.updatedTime = template.updatedTime;
        response.updatedBy = template.updatedBy;
        return response;
    }

    private void components(List<TemplateSectionView> sections, Map<String, PageComponentView> components) {
        for (TemplateSectionView section : sections) {
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
