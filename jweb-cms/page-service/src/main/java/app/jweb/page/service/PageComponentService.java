package app.jweb.page.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.page.api.component.ComponentResponse;
import app.jweb.page.api.component.CreateComponentRequest;

import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Singleton
public class PageComponentService {
    private final Map<String, ComponentResponse> pageComponents = Maps.newConcurrentMap();
    private final List<ComponentResponse> components = Lists.newArrayList();

    public List<ComponentResponse> batchCreate(List<CreateComponentRequest> request) {
        components.clear();
        components.addAll(request.stream().map(this::response).sorted(Comparator.comparing(o -> o.name)).collect(Collectors.toList()));
        for (ComponentResponse component : components) {
            pageComponents.put(component.name, component);
        }
        return components;
    }

    public List<ComponentResponse> find() {
        return components;
    }

    public Optional<ComponentResponse> findByName(String name) {
        return Optional.ofNullable(pageComponents.get(name));
    }

    private ComponentResponse response(CreateComponentRequest request) {
        ComponentResponse response = new ComponentResponse();
        response.id = UUID.randomUUID().toString();
        response.name = request.name;
        response.attributes = request.attributes;
        response.createdBy = request.requestBy;
        response.createdTime = OffsetDateTime.now();
        response.updatedBy = request.requestBy;
        response.updatedTime = OffsetDateTime.now();
        return response;
    }
}
