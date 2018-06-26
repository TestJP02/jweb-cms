package io.sited.page.search.web.component;

import com.google.common.collect.Lists;
import io.sited.page.search.api.PageSearchWebService;
import io.sited.page.search.api.page.SearchPageRequest;
import io.sited.page.search.api.page.SearchPageResponse;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.util.collection.QueryResponse;
import io.sited.web.WebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class PageSearchResultComponent extends WebComponent {
    @Inject
    PageSearchWebService pageSearchWebService;

    public PageSearchResultComponent() {
        super("page-search-result", "component/page-search-result/page-search-result.html",
            Lists.newArrayList(
                new BooleanAttribute("titleEnabled", true),
                new StringAttribute("keyword", null),
                new StringAttribute("sort", null),
                new StringAttribute("categoryId", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);

        String keyword = (String) attribute("keyword").value(attributes);
        if (keyword == null) {
            keyword = request(bindings).queryParam("q").orElse(null);
        }
        String categoryId = (String) attribute("categoryId").value(attributes);
        if (categoryId == null) {
            categoryId = request(bindings).queryParam("categoryId").orElse(null);
        }

        SearchPageRequest request = new SearchPageRequest();
        request.query = keyword;
        request.categoryId = categoryId;

        QueryResponse<SearchPageResponse> pages = pageSearchWebService.search(request);
        scopedBindings.put("keyword", keyword);
        scopedBindings.put("pages", pages);
        template().output(scopedBindings, out);
    }
}
