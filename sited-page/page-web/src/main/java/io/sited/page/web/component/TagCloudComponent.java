package io.sited.page.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.PageTagResponse;
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
public class TagCloudComponent extends TemplateComponent {
    @Inject
    PageTagWebService pageTagWebService;

    public TagCloudComponent() {
        super("tag-cloud", "component/page-tag-cloud/page-tag-cloud.html",
            Lists.newArrayList(new IntegerAttribute("limit", 20), new StringAttribute("title", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        PageTagQuery pageTagQuery = new PageTagQuery();
        pageTagQuery.page = 1;
        pageTagQuery.limit = (Integer) attribute("limit").value(attributes);
        QueryResponse<PageTagResponse> tags = pageTagWebService.find(pageTagQuery);
        Map<String, Object> scopedBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 1);
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        scopedBindings.put("tags", tags);
        template().output(scopedBindings, out);
    }
}
