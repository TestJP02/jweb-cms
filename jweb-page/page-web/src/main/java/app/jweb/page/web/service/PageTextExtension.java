package app.jweb.page.web.service;

import org.commonmark.renderer.html.HtmlRenderer;

/**
 * @author chi
 */
public class PageTextExtension implements HtmlRenderer.HtmlRendererExtension {
    final KeywordService keywordService;

    public PageTextExtension(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(context -> new PageTextRenderer(context, keywordService));
    }
}
