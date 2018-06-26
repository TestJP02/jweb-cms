package io.sited.page.web.service;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Set;

/**
 * @author chi
 */
public class PageTextRenderer implements NodeRenderer {
    private final HtmlWriter html;
    private final KeywordManager keywordManager;

    public PageTextRenderer(HtmlNodeRendererContext context, KeywordManager keywordManager) {
        this.html = context.getWriter();
        this.keywordManager = keywordManager;
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.<Class<? extends Node>>singleton(Text.class);
    }

    @Override
    public void render(Node node) {
        Text text = (Text) node;
        if (text.getParent() != null && text.getParent() instanceof Link) {
            html.text(text.getLiteral());
        } else {
            CreateInnerLinksResult result = keywordManager.createInnerLinks(text.getLiteral());
            if (result.inserted) {
                html.raw(result.result);
            } else {
                html.text(text.getLiteral());
            }
        }
    }
}
