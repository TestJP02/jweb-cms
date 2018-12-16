package app.jweb.post.util;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author chi
 */
public interface Markdown {
    static List<String> imageURLs(String content) {
        if (Strings.isNullOrEmpty(content)) {
            return ImmutableList.of();
        }
        Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
        Node doc = parser.parse(content);
        Set<String> imageURLs = Sets.newHashSet();
        doc.accept(new AbstractVisitor() {
            @Override
            public void visit(Image image) {
                imageURLs.add(image.getDestination());
                super.visit(image);
            }
        });
        return ImmutableList.copyOf(imageURLs);
    }
}
