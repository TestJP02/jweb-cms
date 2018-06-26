package io.sited.page.service;

import com.google.common.collect.Lists;
import io.sited.page.api.layout.BatchUpdateLayoutRequest;
import io.sited.page.api.layout.LayoutRequest;
import io.sited.page.api.layout.LayoutResponse;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageLayoutService {
    private final List<LayoutResponse> layouts = Lists.newArrayList();

    public List<LayoutResponse> find() {
        return layouts;
    }

    @Transactional
    public void batchCreate(BatchUpdateLayoutRequest request) {
        layouts.clear();
        layouts.addAll(request.layouts.stream().map(this::response).collect(Collectors.toList()));
    }

    private LayoutResponse response(LayoutRequest request) {
        LayoutResponse layoutResponse = new LayoutResponse();
        layoutResponse.name = request.name;
        layoutResponse.displayName = request.displayName;
        layoutResponse.gridColumns = request.gridColumns;
        layoutResponse.screenWidths = request.screenWidths;
        layoutResponse.createdBy = request.requestBy;
        layoutResponse.createdTime = OffsetDateTime.now();
        return layoutResponse;
    }

}
