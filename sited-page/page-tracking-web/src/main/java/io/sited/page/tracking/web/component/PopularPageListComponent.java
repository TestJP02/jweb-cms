package io.sited.page.tracking.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageResponse;
import io.sited.page.tracking.api.PageTrackingWebService;
import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
import io.sited.page.tracking.api.tracking.PageTrackingType;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PopularPageListComponent extends TemplateComponent {
    @Inject
    PageTrackingWebService pageTrackingWebService;
    @Inject
    PageWebService pageWebService;

    public PopularPageListComponent() {
        super("popular-page-list", "component/popular-page-list/popular-page-list.html", Lists.newArrayList(new StringAttribute("title", null), new IntegerAttribute("limit", 10)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        String title = (String) attribute("title").value(attributes);
        Integer limit = (Integer) attribute("limit").value(attributes);

        PageTrackingQuery pageVisitQuery = new PageTrackingQuery();
        pageVisitQuery.pageOnly = true;
        pageVisitQuery.limit = limit;

        pageVisitQuery.type = PageTrackingType.DAILY;
        QueryResponse<PageTrackingResponse> dailyResponses = pageTrackingWebService.find(pageVisitQuery);
        List<PageResponse> daily = pageWebService.batchGet(dailyResponses.items.stream().map(item -> item.pageId).collect(Collectors.toList()));

        pageVisitQuery.type = PageTrackingType.WEEKLY;
        QueryResponse<PageTrackingResponse> weeklyResponses = pageTrackingWebService.find(pageVisitQuery);
        List<PageResponse> weekly = pageWebService.batchGet(weeklyResponses.items.stream().map(item -> item.pageId).collect(Collectors.toList()));

        pageVisitQuery.type = PageTrackingType.MONTHLY;
        QueryResponse<PageTrackingResponse> monthlyResponses = pageTrackingWebService.find(pageVisitQuery);
        List<PageResponse> monthly = pageWebService.batchGet(monthlyResponses.items.stream().map(item -> item.pageId).collect(Collectors.toList()));

        Map<String, Object> scopeBindings = Maps.newHashMap();
        scopeBindings.putAll(bindings);
        scopeBindings.put("title", title);
        scopeBindings.put("daily", daily);
        scopeBindings.put("weekly", weekly);
        scopeBindings.put("monthly", monthly);

        template().output(scopeBindings, out);
    }
}
