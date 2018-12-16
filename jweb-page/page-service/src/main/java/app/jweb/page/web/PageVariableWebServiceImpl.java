package app.jweb.page.web;

import com.google.common.collect.ImmutableList;
import app.jweb.page.api.PageVariableWebService;
import app.jweb.page.api.variable.CreateVariableRequest;
import app.jweb.page.api.variable.DeleteVariableRequest;
import app.jweb.page.api.variable.UpdateVariableRequest;
import app.jweb.page.api.variable.VariableFieldView;
import app.jweb.page.api.variable.VariableQuery;
import app.jweb.page.api.variable.VariableResponse;
import app.jweb.page.domain.PageVariable;
import app.jweb.page.service.PageVariableService;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.type.Types;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageVariableWebServiceImpl implements PageVariableWebService {
    @Inject
    PageVariableService pageVariableService;

    @Override
    public VariableResponse get(String id) {
        return response(pageVariableService.get(id));
    }

    @Override
    public QueryResponse<VariableResponse> find(VariableQuery query) {
        return pageVariableService.find(query).map(this::response);
    }

    @Override
    public List<VariableResponse> find() {
        return pageVariableService.find().stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public VariableResponse create(CreateVariableRequest request) {
        return response(pageVariableService.create(request));
    }

    @Override
    public VariableResponse update(String id, UpdateVariableRequest request) {
        return response(pageVariableService.update(id, request));
    }

    @Override
    public void delete(DeleteVariableRequest request) {
        request.ids.forEach(id -> pageVariableService.delete(id, request.requestBy));
    }

    private VariableResponse response(PageVariable request) {
        VariableResponse instance = new VariableResponse();
        instance.id = request.id;
        instance.name = request.name;
        instance.status = request.status;
        instance.fields = request.fields == null ? ImmutableList.of() : JSON.fromJSON(request.fields, Types.generic(List.class, VariableFieldView.class));
        instance.createdBy = request.createdBy;
        instance.createdTime = request.createdTime;
        instance.updatedBy = request.updatedBy;
        instance.updatedTime = request.updatedTime;
        return instance;
    }
}
