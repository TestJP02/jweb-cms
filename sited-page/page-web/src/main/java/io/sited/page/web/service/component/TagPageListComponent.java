package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.service.PageService;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class TagPageListComponent extends AbstractPageComponent {
    @Inject
    PageService pageService;
    @Inject
    PageTagWebService tagWebService;

    public TagPageListComponent() {
        super("tag-page-list", "component/tag-page-list/tag-page-list.html", ImmutableList.of(
            new StringAttribute("tag", null),
            new StringAttribute("title", null),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        String tag = attributes.get("tag");
        if (tag == null) {
            tag = bindings.request().pathParam("tag");
        }
        PageTagResponse response = tagWebService.findByName(tag).orElse(null);
        if (response == null) {
            return;
        }

        String title = attributes.get("title");
        PageQuery pageQuery = new PageQuery();
        pageQuery.tags = Lists.newArrayList(tag);
        pageQuery.page = attributes.get("page");
        pageQuery.limit = attributes.get("limit");
        pageQuery.status = PageStatus.ACTIVE;
        bindings.put("title", title);
        bindings.put("path", response.path == null ? "/tag/" + response.name : response.path);
        bindings.put("pages", pageService.find(pageQuery));
        bindings.put("display", 10);
        bindings.put("limit", pageQuery.limit);
        template().output(bindings, out);
    }
}
