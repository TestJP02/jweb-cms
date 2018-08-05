package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.page.PageRelatedQuery;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.page.web.PageInfo;
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
public class RelatedPageListComponent extends AbstractPageComponent {
    @Inject
    PageService pageService;

    public RelatedPageListComponent() {
        super("related-page-list", "component/related-page-list/related-page-list.html", ImmutableList.of(
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 5)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PageInfo page = bindings.page();

        PageRelatedQuery pageQuery = new PageRelatedQuery();
        pageQuery.limit = attributes.get("limit");
        pageQuery.id = page.id();
        bindings.put("pages", pageService.findRelated(pageQuery));
        template().output(bindings, out);
    }
}
