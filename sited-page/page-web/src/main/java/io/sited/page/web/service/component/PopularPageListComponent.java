package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PopularPageQuery;
import io.sited.page.api.page.RankType;
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
public class PopularPageListComponent extends AbstractPageComponent {
    @Inject
    PageWebService pageWebService;

    public PopularPageListComponent() {
        super("popular-page-list", "component/popular-page-list/popular-page-list.html", Lists.newArrayList(
            new StringAttribute("title", null),
            new IntegerAttribute("limit", 10),
            new StringAttribute("type", "DAILY")
        ));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String title = attributes.get("title");
        Integer limit = attributes.get("limit");
        String page = bindings.request().queryParam("page").orElse(null);
        String type = attributes.get("type");

        PopularPageQuery query = new PopularPageQuery();
        query.limit = limit;
        query.page = page == null ? 1 : Integer.parseInt(page);
        query.type = type == null ? null : RankType.valueOf(type);
        QueryResponse<PageResponse> pages = pageWebService.popular(query);

        bindings.put("title", title);
        bindings.put("pages", pages);
        template().output(bindings, out);
    }
}
