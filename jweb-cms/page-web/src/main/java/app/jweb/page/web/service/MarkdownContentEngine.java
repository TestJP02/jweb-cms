package app.jweb.page.web.service;

import app.jweb.page.web.ContentEngine;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * @author chi
 */
public class MarkdownContentEngine implements ContentEngine {
    @Inject
    KeywordService keywordService;
    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private HtmlRenderer renderer;

    @Override
    public String render(String content) {
        Node document = parser.parse(content);
        return renderer().render(document);
    }

    private HtmlRenderer renderer() {
        if (renderer == null) {
            renderer = HtmlRenderer.builder().extensions(Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                InsExtension.create(),
                HeadingAnchorExtension.create(),
                new PageTextExtension(keywordService))).build();
        }
        return renderer;
    }
}
