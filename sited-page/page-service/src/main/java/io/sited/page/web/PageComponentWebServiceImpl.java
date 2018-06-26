package io.sited.page.web;

import io.sited.page.api.PageComponentWebService;
import io.sited.page.api.component.ComponentResponse;
import io.sited.page.api.component.CreateComponentRequest;
import io.sited.page.service.PageComponentService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author chi
 */
public class PageComponentWebServiceImpl implements PageComponentWebService {
    @Inject
    PageComponentService pageComponentService;

    @Override
    public List<ComponentResponse> find() {
        return pageComponentService.find();
    }

    @Override
    public List<ComponentResponse> batchCreate(List<CreateComponentRequest> request) {
        return pageComponentService.batchCreate(request);
    }
}
