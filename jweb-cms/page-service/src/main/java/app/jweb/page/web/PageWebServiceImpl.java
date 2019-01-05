package app.jweb.page.web;

import app.jweb.page.api.PageWebService;
import app.jweb.page.api.component.PostComponentView;
import app.jweb.page.api.page.BatchCreatePageRequest;
import app.jweb.page.api.page.BatchDeletePageRequest;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PageSectionView;
import app.jweb.page.api.page.UpdatePageRequest;
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
public class PageWebServiceImpl implements PageWebService {
    @Inject
    PageTemplateService pageTemplateService;
    @Inject
    PageSavedComponentService pageSavedComponentService;
    @Inject
    PageComponentService pageComponentService;

    @Override
    public PageResponse get(String id) {
        return response(pageTemplateService.get(id));
    }

    @Override
    public Optional<PageResponse> findByTemplatePath(String templatePath) {
        Optional<PageTemplate> pageTemplate = pageTemplateService.findByTemplatePath(templatePath);
        return pageTemplate.map(this::response);
    }

    @Override
    public Optional<PageResponse> findByPath(String path) {
        Optional<PageTemplate> pageTemplate = pageTemplateService.findByPath(path);
        return pageTemplate.map(this::response);
    }

    @Override
    public QueryResponse<PageResponse> find(PageQuery query) {
        return pageTemplateService.find(query).map(this::response);
    }

    @Override
    public PageResponse create(CreatePageRequest request) {
        return response(pageTemplateService.create(request));
    }

    @Override
    public void batchCreate(BatchCreatePageRequest request) {
        pageTemplateService.batchCreate(request);
    }

    @Override
    public PageResponse update(String id, UpdatePageRequest request) {
        return response(pageTemplateService.update(id, request));
    }

    @Override
    public void batchDelete(BatchDeletePageRequest request) {
        request.ids.forEach(id -> pageTemplateService.delete(id, request.requestBy));
    }

    private PageResponse response(PageTemplate template) {
        PageResponse response = new PageResponse();
        response.id = template.id;
        response.userId = template.userId;
        response.path = template.path;
        response.templatePath = template.templatePath;
        response.title = template.title;
        response.description = template.description;
        response.tags = template.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(template.tags);
        response.type = template.type;
        if (template.sections != null) {
            response.sections = JSON.fromJSON(template.sections, Types.generic(List.class, PageSectionView.class));
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

    private void components(List<PageSectionView> sections, Map<String, PostComponentView> components) {
        for (PageSectionView section : sections) {
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
