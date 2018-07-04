package io.sited.page.archive.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.archive.api.PageArchiveWebService;
import io.sited.page.archive.api.archive.PageArchiveResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * @author chi
 */
public class ArchivePageListComponent extends AbstractPageComponent {
    @Inject
    PageWebService pageWebservice;

    @Inject
    PageArchiveWebService pageArchiveWebService;

    public ArchivePageListComponent() {
        super("archive-page-list", "component/archive-page-list/archive-page-list.html",
            ImmutableList.of(
                new StringAttribute("title", null),
                new StringAttribute("name", null),
                new IntegerAttribute("page", 1),
                new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        String name = bindings.request().pathParam("name");
        Optional<PageArchiveResponse> archiveOptional = pageArchiveWebService.findByName(name);
        if (!archiveOptional.isPresent()) {
            return;
        }
        PageArchiveResponse archive = archiveOptional.get();
        String title = attributes.get("title");
        PageQuery pageQuery = new PageQuery();
        pageQuery.page = attributes.get("page");
        pageQuery.limit = attributes.get("limit");
        pageQuery.status = PageStatus.ACTIVE;
        OffsetDateTime startTime = archive.timestamp;
        OffsetDateTime endTime = startTime.plusMonths(1).minusDays(1);
        pageQuery.startTime = startTime;
        pageQuery.endTime = endTime;

        bindings.put("title", title);
        bindings.put("path", "/archive/" + name + "/");
        bindings.put("pages", pageWebservice.find(pageQuery));
        bindings.put("display", 10);
        bindings.put("limit", pageQuery.limit);
        template().output(bindings, out);
    }
}
