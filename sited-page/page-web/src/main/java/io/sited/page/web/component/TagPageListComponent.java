package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.page.api.PageTagWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.web.service.CachedPageService;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class TagPageListComponent extends TemplateComponent {
    @Inject
    CachedPageService cachedPageService;
    @Inject
    PageTagWebService tagWebService;

    public TagPageListComponent() {
        super("tag-page-list", "component/page-list/page-list.html", ImmutableList.of(
            new StringAttribute("tag", null),
            new StringAttribute("title", null),
            new IntegerAttribute("page", 1),
            new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);

        String tag = (String) attribute("tag").value(attributes);
        if (tag == null) {
            return;
        }
        PageTagResponse response = tagWebService.findByName(tag).orElse(null);
        if (response == null) {
            return;
        }

        String title = (String) attribute("title").value(attributes);
        if (title == null) {
            title = tag;
        }

        PageQuery pageQuery = new PageQuery();
        pageQuery.tags = Lists.newArrayList(tag);
        pageQuery.page = (Integer) attribute("page").value(attributes);
        pageQuery.limit = (Integer) attribute("limit").value(attributes);
        pageQuery.status = PageStatus.ACTIVE;
        scopedBindings.put("title", title);
        scopedBindings.put("path", response.path == null ? "/tag/" + response.name : response.path);
        scopedBindings.put("pages", cachedPageService.find(pageQuery));
        scopedBindings.put("display", 10);
        scopedBindings.put("limit", pageQuery.limit);
        template().output(scopedBindings, out);
    }
}
