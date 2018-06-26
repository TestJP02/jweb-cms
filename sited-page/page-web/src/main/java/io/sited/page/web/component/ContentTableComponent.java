package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.web.service.CachedPageContentService;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.heading.anchor.IdGenerator;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class ContentTableComponent extends TemplateComponent {
    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private final IdGenerator idGenerator = IdGenerator.builder().build();
    @Inject
    CachedPageContentService cachedPageContentService;

    public ContentTableComponent() {
        super("content-table", "component/page-content-table/page-content-table.html", ImmutableList.of(
            new StringAttribute("title", null), new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream output) throws IOException {
        PageResponse page = page(bindings, attributes);
        if (page == null) return;

        PageContentResponse content = cachedPageContentService.content(page.id, isDraft(page));
        Node document = parser.parse(content.content);
        Node child = document.getFirstChild();
        List<HeadingView> menu = Lists.newArrayList();
        do {
            if (child instanceof Heading) {
                HeadingView heading = new HeadingView();
                heading.title = ((Text) (child.getFirstChild())).getLiteral();
                heading.level = ((Heading) child).getLevel();
                if (menu.isEmpty()) {
                    menu.add(heading);
                } else {
                    fillMenu(menu, heading);
                }
            }
            child = child.getNext();
        } while (child != null);
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        scopedBindings.put("content", output(menu));
        template().output(scopedBindings, output);
    }

    private PageResponse page(Map<String, Object> bindings, Map<String, Object> attributes) {
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return null;
        }
        return page;
    }

    private String output(List<HeadingView> headingList) {
        if (headingList.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(256);
        builder.append("<ul class=\"page-content-table__list\">");
        headingList.forEach(heading -> {
            builder.append("<li class=\"page-content-table__item\">")
                .append("<a class=\"page-content-table__item-link\" href=\"#")
                .append(idGenerator.generateId(heading.title))
                .append("\">")
                .append(heading.title)
                .append("</a>");
            if (heading.children != null) {
                builder.append(output(heading.children));
            }
            builder.append("</li>");
        });
        builder.append("</ul>");
        return builder.toString();
    }

    private void fillMenu(List<HeadingView> menu, HeadingView heading) {
        HeadingView prev = menu.get(menu.size() - 1);
        if (heading.level <= prev.level) {
            menu.add(heading);
            return;
        }
        while (heading.level > prev.level) {
            if (prev.children == null) {
                prev.children = Lists.newArrayList();
                prev.children.add(heading);
                return;
            } else if (heading.level <= prev.children.get(prev.children.size() - 1).level) {
                prev.children.add(heading);
                return;
            } else {
                prev = prev.children.get(prev.children.size() - 1);
            }
        }
    }

    private boolean isDraft(PageResponse page) {
        return page.status == PageStatus.DRAFT || page.status == PageStatus.AUDITING;
    }

    public static class HeadingView {
        public String title;
        public int level;
        public List<HeadingView> children;
    }
}
