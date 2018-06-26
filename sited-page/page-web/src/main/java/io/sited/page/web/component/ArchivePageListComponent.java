package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.archive.PageArchiveResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageStatus;
import io.sited.page.api.tag.PageTagResponse;
import io.sited.page.web.service.CachedPageService;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class ArchivePageListComponent extends TemplateComponent {
    @Inject
    CachedPageService pageManager;

    public ArchivePageListComponent() {
        super("archive-page-list", "component/page-list/page-list.html",
            ImmutableList.of(
                new ObjectAttribute<PageTagResponse>("archive", PageArchiveResponse.class, null),
                new StringAttribute("title", null),
                new IntegerAttribute("page", 1),
                new IntegerAttribute("limit", 20)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);

        PageArchiveResponse archive = (PageArchiveResponse) attribute("archive").value(attributes);
        if (archive == null) {
            return;
        }

        String title = (String) attribute("title").value(attributes);
        if (title == null) {
            title = archive.year + "/" + archive.month;
        }

        PageQuery pageQuery = new PageQuery();
        pageQuery.page = (Integer) attribute("page").value(attributes);
        pageQuery.limit = (Integer) attribute("limit").value(attributes);
        pageQuery.status = PageStatus.ACTIVE;
        OffsetDateTime startTime = OffsetDateTime.parse(archive.year + "/" + archive.month, DateTimeFormatter.ofPattern("yyyy/MM"));
        OffsetDateTime endTime = startTime.plusMonths(1).minusDays(1);
        pageQuery.startTime = startTime;
        pageQuery.endTime = endTime;

        scopedBindings.put("title", title);
        scopedBindings.put("path", "/archive/" + archive.year + "-" + archive.month + "/");
        scopedBindings.put("pages", pageManager.find(pageQuery));
        scopedBindings.put("display", 10);
        scopedBindings.put("limit", pageQuery.limit);
        template().output(scopedBindings, out);
    }
}
