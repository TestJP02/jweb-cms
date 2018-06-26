package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author chi
 */
public class PageCardComponent extends TemplateComponent {
    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();

    public PageCardComponent() {
        super("card", "component/page-card/page-card.html", ImmutableList.of(
            new StringAttribute("title", null),
            new StringAttribute("content", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        String content = (String) attribute("content").value(attributes);
        Node document = parser.parse(content);
        String htmlContent = renderer.render(document);
        scopedBindings.put("content", htmlContent);
        template().output(scopedBindings, out);
    }
}
