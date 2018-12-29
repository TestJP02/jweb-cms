package app.jweb.page.web.service;

import app.jweb.cache.Cache;
import app.jweb.page.api.PageVariableWebService;
import app.jweb.page.api.variable.VariableFieldType;
import app.jweb.page.api.variable.VariableFieldView;
import app.jweb.page.api.variable.VariableResponse;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class VariableCacheService {
    private static final String VARIABLE_CACHE_KEY = "all";
    @Inject
    PageVariableWebService pageVariableWebService;

    @Inject
    Cache<VariableWrapper> cache;

    public Map<String, Object> variables() {
        Optional<VariableWrapper> cached = cache.get(VARIABLE_CACHE_KEY);
        if (cached.isPresent()) {
            return cached.get().variables;
        }

        List<VariableResponse> variableResponses = pageVariableWebService.find();
        Map<String, Object> variables = Maps.newHashMap();
        variableResponses.forEach(variableResponse -> variables.put(variableResponse.name, variable(variableResponse)));
        VariableWrapper variableWrapper = new VariableWrapper();
        variableWrapper.variables = variables;
        cache.put(VARIABLE_CACHE_KEY, variableWrapper);

        return variables;
    }

    public void reloadAll() {
        cache.delete(VARIABLE_CACHE_KEY);
    }

    private Map<String, Object> variable(VariableResponse variableResponse) {
        Map<String, Object> values = Maps.newHashMap();
        variableResponse.fields.forEach(variableFieldView -> values.put(variableFieldView.name, value(variableFieldView)));
        return values;
    }

    private Object value(VariableFieldView field) {
        if (field.value == null) {
            return null;
        }
        if (field.type == VariableFieldType.INTEGER) {
            return Integer.parseInt(field.value);
        } else if (field.type == VariableFieldType.BOOLEAN) {
            return Boolean.parseBoolean(field.value);
        } else if (field.type == VariableFieldType.DOUBLE) {
            return Double.parseDouble(field.value);
        } else if (field.type == VariableFieldType.LONG) {
            return Long.parseLong(field.value);
        } else {
            return field.value;
        }
    }

    public static class VariableWrapper {
        public Map<String, Object> variables;
    }
}
