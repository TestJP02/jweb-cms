package io.sited.page.search.web.component;

import com.google.common.collect.Lists;
import io.sited.page.search.api.PageSearchWebService;
import io.sited.page.search.api.page.SearchPageRequest;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class SearchPageResultComponent extends AbstractPageComponent {
    @Inject
    PageSearchWebService pageSearchWebService;

    public SearchPageResultComponent() {
        super("search-page-results", "component/page-search-result/page-search-result.html",
            Lists.newArrayList(
                new StringAttribute("keyword", null),
                new StringAttribute("sort", null),
                new StringAttribute("categoryId", null)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        String keyword = attributes.get("keyword");
        if (keyword == null) {
            keyword = bindings.request().queryParam("q").orElse(null);
        }
        String categoryId = attributes.get("categoryId");
        if (categoryId == null) {
            categoryId = bindings.request().queryParam("categoryId").orElse(null);
        }
        SearchPageRequest request = new SearchPageRequest();
        request.query = keyword;
        request.categoryId = categoryId;
        scopedBindings.put("pages", pageSearchWebService.search(request));
        scopedBindings.put("keyword", keyword);
        template().output(scopedBindings, out);
    }
}
