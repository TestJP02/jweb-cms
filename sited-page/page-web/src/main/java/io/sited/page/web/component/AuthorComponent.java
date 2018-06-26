package io.sited.page.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class AuthorComponent extends TemplateComponent {
    @Inject
    PageWebService pageWebService;

    public AuthorComponent() {
        super("author", "component/page-author/page-author.html",
            Lists.newArrayList(new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }
        Map<String, Object> scopedBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 1);
        PageQuery query = new PageQuery();
        query.userId = page.userId;
        query.limit = 5;
        QueryResponse<PageResponse> pages = pageWebService.find(query);
        scopedBindings.put("pages", pages);
        template().output(scopedBindings, out);
    }
}
