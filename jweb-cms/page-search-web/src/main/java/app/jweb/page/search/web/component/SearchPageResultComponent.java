package app.jweb.page.search.web.component;

import app.jweb.page.search.api.PageSearchWebService;
import app.jweb.page.search.api.page.SearchPageQuery;
import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class SearchPageResultComponent extends AbstractPostComponent {
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
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
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
        SearchPageQuery request = new SearchPageQuery();
        request.query = keyword;
        request.categoryId = categoryId;
        scopedBindings.put("pages", pageSearchWebService.search(request));
        scopedBindings.put("keyword", keyword);
        template().output(scopedBindings, out);
    }
}
