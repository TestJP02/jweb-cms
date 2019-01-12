package app.jweb.page.web;

import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.template.PageTemplateResponse;
import app.jweb.page.web.service.PageTemplateRepository;
import app.jweb.page.web.service.VariableCacheService;
import app.jweb.web.AbstractWebController;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author chi
 */
public abstract class AbstractPageController extends AbstractWebController {
    @Inject
    VariableCacheService variableService;
    @Inject
    PageTemplateWebService pageTemplateWebService;

    protected Response.ResponseBuilder page(PageResponse page) {
        return page(page, ImmutableMap.of());
    }

    protected Response.ResponseBuilder page(PageResponse page, Map<String, Object> bindings) {
        Map<String, Object> variables = variableService.variables();
        Map<String, Object> pageBindings = Maps.newHashMapWithExpectedSize(bindings.size() + variables.size() + 2);
        pageBindings.putAll(bindings);
        pageBindings.putAll(variables);
        pageBindings.put("page", variable(page));
        pageBindings.put("client", clientInfo);
        pageBindings.put("template", variable(pageTemplateWebService.get(page.id)));
        return template(PageTemplateRepository.path(page.id), pageBindings);
    }

    private PageInfo variable(PageResponse page) {
        PageInfo variable = new PageInfo();
        variable.id = page.id;
        variable.userId = page.userId;
        variable.categoryId = page.categoryId;
        variable.path = page.path;
        variable.title = page.title;
        variable.description = page.description;
        variable.tags = page.tags;
        variable.keywords = Lists.newArrayList();
        variable.status = page.status;
        variable.createdTime = page.createdTime;
        variable.createdBy = page.createdBy;
        variable.updatedTime = page.updatedTime;
        variable.updatedBy = page.updatedBy;
        return variable;
    }

    private TemplateVariable variable(PageTemplateResponse template) {
        TemplateVariable variable = new TemplateVariable();
        variable.components = template.components;
        return variable;
    }
}
