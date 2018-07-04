package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class TagCloudComponent extends AbstractPageComponent {
    @Inject
    PageTagWebService pageTagWebService;

    public TagCloudComponent() {
        super("tag-cloud", "component/page-tag-cloud/page-tag-cloud.html",
            Lists.newArrayList(new IntegerAttribute("limit", 20), new StringAttribute("title", null)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageTagQuery pageTagQuery = new PageTagQuery();
        pageTagQuery.page = 1;
        pageTagQuery.limit = attributes.get("limit");
        QueryResponse<PageTagResponse> tags = pageTagWebService.find(pageTagQuery);
        bindings.putAll(attributes);
        bindings.put("tags", tags);
        template().output(bindings, out);
    }
}
