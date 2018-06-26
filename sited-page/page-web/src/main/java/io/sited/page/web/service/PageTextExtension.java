package io.sited.page.web.service;

import org.commonmark.renderer.html.HtmlRenderer;

/**
 * @author chi
 */
public class PageTextExtension implements HtmlRenderer.HtmlRendererExtension {
    final KeywordManager keywordManager;

    public PageTextExtension(KeywordManager keywordManager) {
        this.keywordManager = keywordManager;
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(context -> new PageTextRenderer(context, keywordManager));
    }
}
