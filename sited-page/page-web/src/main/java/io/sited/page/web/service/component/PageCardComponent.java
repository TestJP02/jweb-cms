package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.web.AbstractPageComponent;
import io.sited.template.Attributes;
import io.sited.page.web.ComponentBindings;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
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

/**
 * @author chi
 */
public class PageCardComponent extends AbstractPageComponent {
    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();

    public PageCardComponent() {
        super("card", "component/page-card/page-card.html", ImmutableList.of(
            new StringAttribute("title", null),
            new StringAttribute("content", null)));
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        String content = attributes.get("content");
        Node document = parser.parse(content);
        String htmlContent = renderer.render(document);
        bindings.put("content", htmlContent);
        template().output(bindings, out);
    }
}
