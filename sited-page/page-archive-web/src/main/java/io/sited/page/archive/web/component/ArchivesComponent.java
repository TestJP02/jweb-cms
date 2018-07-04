package io.sited.page.archive.web.component;

import com.google.common.collect.Lists;
import io.sited.page.archive.api.PageArchiveWebService;
import io.sited.page.archive.api.archive.PageArchiveQuery;
import io.sited.page.archive.api.archive.PageArchiveResponse;
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
public class ArchivesComponent extends AbstractPageComponent {
    @Inject
    PageArchiveWebService pageArchiveWebService;

    public ArchivesComponent() {
        super("archives", "component/page-archive/page-archive.html", Lists.newArrayList(
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 20)
        ));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String title = attributes.get("title");
        Integer limit = attributes.get("limit");
        PageArchiveQuery pageArchiveQuery = new PageArchiveQuery();
        pageArchiveQuery.page = 1;
        pageArchiveQuery.limit = limit;
        QueryResponse<PageArchiveResponse> queryResponse = pageArchiveWebService.find(pageArchiveQuery);
        bindings.put("title", title);
        bindings.put("archives", queryResponse.items);
        template().output(bindings, out);
    }
}
