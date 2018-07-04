package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.IntegerAttribute;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import io.sited.util.collection.QueryResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PagePaginationComponent extends TemplateComponent {
    public PagePaginationComponent() {
        super("pagination", "component/page-pagination/page-pagination.html",
            Lists.newArrayList(new StringAttribute("path", "/"),
                new IntegerAttribute("display", 10),
                new ObjectAttribute<>("items", QueryResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        QueryResponse items = attributes.get("items");
        if (items == null) {
            return;
        }

        int display = attributes.get("display");
        if (items.total <= display) {
            return;
        }

        String path = attributes.get("path");
        int current = items.page == null ? 1 : items.page;
        int limit = items.limit == null ? 20 : items.limit;
        long total = items.total % limit == 0 ? items.total / limit : items.total / limit + 1;

        int start = current - display / 2 > 0 ? current - display / 2 : 1;
        int end = current + display / 2 < total ? current + display / 2 : (int) total;

        PaginationPathBuilder pathBuilder = new PaginationPathBuilder(path);
        PaginationView paginationView = new PaginationView();
        if (current > 1) {
            PaginationLinkView prev = new PaginationLinkView();
            prev.link = pathBuilder.path(current - 1);
            paginationView.prev = prev;
        }

        paginationView.items = Lists.newArrayList();
        for (int i = start; i <= end; i++) {
            PaginationLinkView link = new PaginationLinkView();
            link.displayName = String.valueOf(i);
            link.link = pathBuilder.path(i);
            link.selected = i == current;
            paginationView.items.add(link);
        }

        if (end < total) {
            PaginationLinkView next = new PaginationLinkView();
            next.link = pathBuilder.path(current + 1);
            paginationView.next = next;
        }

        Map<String, Object> scopeBindings = Maps.newHashMapWithExpectedSize(bindings.size() + 1);
        scopeBindings.putAll(bindings);
        scopeBindings.put("pagination", paginationView);
        template().output(scopeBindings, out);
    }

    public static class PaginationPathBuilder {
        private final String basePath;
        private final boolean queryParams;

        PaginationPathBuilder(String bastPath) {
            this.basePath = bastPath;
            queryParams = bastPath.contains("?");
        }

        String path(Integer page) {
            if (page == 1) {
                return basePath;
            }
            if (queryParams) {
                return basePath + "&page=" + page;
            } else {
                return basePath + "?page=" + page;
            }
        }
    }

    public static class PaginationView {
        public PaginationLinkView prev;
        public PaginationLinkView first;
        public List<PaginationLinkView> items;
        public List<PaginationLinkView> middleItems;
        public PaginationLinkView last;
        public PaginationLinkView next;
    }

    public static class PaginationLinkView {
        public String displayName;
        public String link;
        public Boolean selected;
    }
}
