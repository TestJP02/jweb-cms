package app.jweb.page.web;

import app.jweb.page.api.PageComponentWebService;
import app.jweb.page.api.component.ComponentResponse;
import app.jweb.page.api.component.CreateComponentRequest;
import app.jweb.page.service.PageComponentService;

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
