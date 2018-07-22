package io.sited.page.web.service;

import com.google.common.collect.Lists;
import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.keyword.KeywordResponse;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author chi
 */
public class PageTextExtensionTest {
    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private HtmlRenderer renderer;

    @BeforeEach
    public void setup() {
        PageKeywordWebService pageKeywordWebService = Mockito.mock(PageKeywordWebService.class);
        when(pageKeywordWebService.find()).thenReturn(keywords());
        KeywordManager keyManager = new KeywordManager().setPageKeywordWebService(pageKeywordWebService);
        keyManager.start();
        renderer = HtmlRenderer.builder().extensions(Arrays.asList(
            TablesExtension.create(),
            AutolinkExtension.create(),
            StrikethroughExtension.create(),
            InsExtension.create(),
            HeadingAnchorExtension.create(),
            new PageTextExtension(keyManager))).build();
    }

    @Test
    public void test() {
        String content = "# PageTitle\n"
            + "## Sub Title\n"
            + "============\n"
            + "this is a page <br>content link 32<br>[this is a link](\"https://www.baidu.com\" \"this is a link\")<br>I have a keyword\n";
        Node document = parser.parse(content);
        String htmlContent = renderer.render(document);
        String expected = "<h1 id=\"pagetitle\">PageTitle</h1>\n"
            + "<h2 id=\"sub-title\">Sub Title</h2>\n"
            + "<p>============\n"
            + "this is a page <br>content <a href=\"https://www.baidu.com\">link</a> 32<br><a href=\"&quot;https://www.baidu.com&quot;\" title=\"this is a link\">this is a link</a><br>I have a keyword</p>\n";
        assertEquals(expected, htmlContent);
    }

    private List<KeywordResponse> keywords() {
        List<KeywordResponse> keywords = Lists.newArrayList();
        KeywordResponse keyword = new KeywordResponse();
        keyword.path = "https://www.baidu.com";
        keyword.value = "link";
        keywords.add(keyword);
        return keywords;
    }

}