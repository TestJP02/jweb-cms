//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPageComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.template.Attributes;
//import app.jweb.template.Children;
//import app.jweb.template.StringAttribute;
//import com.google.common.collect.ImmutableList;
//import org.commonmark.ext.autolink.AutolinkExtension;
//import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
//import org.commonmark.ext.gfm.tables.TablesExtension;
//import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
//import org.commonmark.ext.ins.InsExtension;
//import org.commonmark.node.Node;
//import org.commonmark.parser.Parser;
//import org.commonmark.renderer.html.HtmlRenderer;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Arrays;
//
///**
// * @author chi
// */
//public class PostCardComponent extends AbstractPageComponent {
//    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
//    private final HtmlRenderer renderer = HtmlRenderer.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
//
//    public PostCardComponent() {
//        super("card", "component/post-card/post-card.html", ImmutableList.of(
//            new StringAttribute("title", null),
//            new StringAttribute("content", null)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        bindings.putAll(attributes);
//        String content = attributes.get("content");
//        Node document = parser.parse(content);
//        String htmlContent = renderer.render(document);
//        bindings.put("content", htmlContent);
//        template().output(bindings, out);
//    }
//}
