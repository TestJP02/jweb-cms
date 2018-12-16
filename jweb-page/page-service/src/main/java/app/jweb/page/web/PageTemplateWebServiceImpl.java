package app.jweb.page.web;

import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.component.PostComponentView;
import app.jweb.page.api.template.BatchCreateTemplateRequest;
import app.jweb.page.api.template.BatchDeletePageRequest;
import app.jweb.page.api.template.CreateTemplateRequest;
import app.jweb.page.api.template.TemplateQuery;
import app.jweb.page.api.template.TemplateResponse;
import app.jweb.page.api.template.TemplateSectionView;
import app.jweb.page.api.template.UpdateTemplateRequest;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.page.domain.PageTemplate;
import app.jweb.page.service.PageComponentService;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.page.service.PageTemplateService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.type.Types;
import com.google.common.base.Splitter;
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
    @Inject
    PageComponentService pageComponentService;

    @Override
    public TemplateResponse get(String id) {
        return response(pageTemplateService.get(id));
    }

    @Override
    public Optional<TemplateResponse> findByTemplatePath(String templatePath) {
        Optional<PageTemplate> pageTemplate = pageTemplateService.findByTemplatePath(templatePath);
        return pageTemplate.map(this::response);
    }

    @Override
    public Optional<TemplateResponse> findByPath(String path) {
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
        response.userId = template.userId;
        response.path = template.path;
        response.templatePath = template.templatePath;
        response.title = template.title;
        response.description = template.description;
        response.tags = template.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(template.tags);
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

    private void components(List<TemplateSectionView> sections, Map<String, PostComponentView> components) {
        for (TemplateSectionView section : sections) {
            if (section.components != null) {
                for (PostComponentView component : section.components) {
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
