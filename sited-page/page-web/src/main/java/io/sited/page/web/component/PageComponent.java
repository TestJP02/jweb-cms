package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.web.service.CachedPageContentService;
import io.sited.page.web.service.KeywordManager;
import io.sited.page.web.service.PageTextExtension;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author chi
 */
public class PageComponent extends TemplateComponent {
    @Inject
    CachedPageContentService cachedPageContentService;
    @Inject
    KeywordManager keywordManager;

    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private HtmlRenderer renderer;


    public PageComponent() {
        super("page", "component/page-page/page-page.html", ImmutableList.of(
            new ObjectAttribute<>("page", PageResponse.class, null),
            new BooleanAttribute("titleEnabled", true)));
    }

    private HtmlRenderer renderer() {
        if (renderer == null) {
            renderer = HtmlRenderer.builder().extensions(Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                InsExtension.create(),
                HeadingAnchorExtension.create(),
                new PageTextExtension(keywordManager))).build();
        }
        return renderer;
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }
        Boolean titleEnabled = (Boolean) attribute("titleEnabled").value(attributes);
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.put("page", page);
        scopedBindings.put("titleEnabled", titleEnabled);

        PageContentResponse content = cachedPageContentService.content(page.id, isDraft(page));
        Node document = parser.parse(content.content);
        String htmlContent = renderer().render(document);
        scopedBindings.put("content", htmlContent);
        template().output(scopedBindings, out);
    }

    private boolean isDraft(PageResponse page) {
        return page.status == PageStatus.DRAFT || page.status == PageStatus.AUDITING;
    }
}
