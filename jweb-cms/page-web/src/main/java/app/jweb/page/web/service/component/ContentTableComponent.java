//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPageComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.page.web.PageInfo;
//import app.jweb.template.Attributes;
//import app.jweb.template.Children;
//import app.jweb.template.StringAttribute;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.Lists;
//import org.commonmark.ext.autolink.AutolinkExtension;
//import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
//import org.commonmark.ext.gfm.tables.TablesExtension;
//import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
//import org.commonmark.ext.heading.anchor.IdGenerator;
//import org.commonmark.ext.ins.InsExtension;
//import org.commonmark.node.Heading;
//import org.commonmark.node.Node;
//import org.commonmark.node.Text;
//import org.commonmark.parser.Parser;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author chi
// */
//
//public class ContentTableComponent extends AbstractPageComponent {
//    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
//    private final IdGenerator idGenerator = IdGenerator.builder().build();
//
//    public ContentTableComponent() {
//        super("content-table", "component/content-table/content-table.html", ImmutableList.of(
//            new StringAttribute("title", null)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream output) throws IOException {
//        PageInfo post = bindings.post();
//        if (post.content() == null) {
//            return;
//        }
//
//        Node document = parser.parse(post.content());
//        Node child = document.getFirstChild();
//        List<HeadingView> menu = Lists.newArrayList();
//        while (child != null) {
//            if (child instanceof Heading) {
//                HeadingView heading = new HeadingView();
//                heading.title = ((Text) (child.getFirstChild())).getLiteral();
//                heading.level = ((Heading) child).getLevel();
//                if (menu.isEmpty()) {
//                    menu.add(heading);
//                } else {
//                    fillMenu(menu, heading);
//                }
//            }
//            child = child.getNext();
//        }
//        bindings.putAll(attributes);
//        bindings.put("content", output(menu));
//        template().output(bindings, output);
//    }
//
//    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
//    private String output(List<HeadingView> headingList) {
//        if (headingList.isEmpty()) {
//            return "";
//        }
//        StringBuilder builder = new StringBuilder(256);
//        builder.append("<ul class=\"content-table__list\">");
//        headingList.forEach(heading -> {
//            builder.append("<li class=\"content-table__item\"><a class=\"content-table__item-link\" href=\"#")
//                .append(idGenerator.generateId(heading.title))
//                .append("\">")
//                .append(heading.title)
//                .append("</a>");
//            if (heading.children != null) {
//                builder.append(output(heading.children));
//            }
//            builder.append("</li>");
//        });
//        builder.append("</ul>");
//        return builder.toString();
//    }
//
//    private void fillMenu(List<HeadingView> menu, HeadingView heading) {
//        HeadingView prev = menu.get(menu.size() - 1);
//        if (heading.level <= prev.level) {
//            menu.add(heading);
//            return;
//        }
//        while (heading.level > prev.level) {
//            if (prev.children == null) {
//                prev.children = Lists.newArrayList();
//                prev.children.add(heading);
//                return;
//            } else if (heading.level <= prev.children.get(prev.children.size() - 1).level) {
//                prev.children.add(heading);
//                return;
//            } else {
//                prev = prev.children.get(prev.children.size() - 1);
//            }
//        }
//    }
//
//    public static class HeadingView {
//        public String title;
//        public int level;
//        public List<HeadingView> children;
//    }
//}
