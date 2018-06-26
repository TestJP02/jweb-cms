package io.sited.page.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageArchiveWebService;
import io.sited.page.api.archive.PageArchiveQuery;
import io.sited.page.api.archive.PageArchiveResponse;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class PageArchiveComponent extends TemplateComponent {
    @Inject
    PageArchiveWebService pageArchiveWebService;

    public PageArchiveComponent() {
        super("archive-list", "component/page-archive/page-archive.html", Lists.newArrayList(
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 20)
        ));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        String title = (String) attributes.get("title");
        Integer limit = (Integer) attributes.get("limit");
        PageArchiveQuery pageArchiveQuery = new PageArchiveQuery();
        pageArchiveQuery.page = 1;
        pageArchiveQuery.limit = limit;
        QueryResponse<PageArchiveResponse> queryResponse = pageArchiveWebService.find(pageArchiveQuery);
        Map<String, Object> scopeBindings = Maps.newHashMap();
        scopeBindings.putAll(bindings);
        scopeBindings.put("title", title);
        scopeBindings.put("archives", queryResponse.items);
        template().output(scopeBindings, out);
    }
}
