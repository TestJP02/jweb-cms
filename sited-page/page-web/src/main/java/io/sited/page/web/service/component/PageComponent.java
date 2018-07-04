package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.PageStatisticsWebService;
import io.sited.page.api.statistics.PageStatisticsResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.PageInfo;
import io.sited.page.web.service.KeywordManager;
import io.sited.page.web.service.PageTextExtension;
import io.sited.template.Attributes;
import io.sited.template.BooleanAttribute;
import io.sited.template.Children;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author chi
 */
public class PageComponent extends AbstractPageComponent {
    @Inject
    KeywordManager keywordManager;

    @Inject
    PageStatisticsWebService pageStatisticsWebService;

    private final Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create(), InsExtension.create(), HeadingAnchorExtension.create())).build();
    private HtmlRenderer renderer;


    public PageComponent() {
        super("page", "component/page-page/page-page.html", ImmutableList.of(
            new BooleanAttribute("titleEnabled", true),
            new BooleanAttribute("statisticsEnabled", true)));
    }

    private HtmlRenderer renderer() {
        if (renderer == null) {
            renderer = HtmlRenderer.builder().extensions(Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                InsExtension.create(),
                HeadingAnchorExtension.create(),
                new PageTextExtension(keywordManager))).build();
        }
        return renderer;
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PageInfo page = bindings.page();
        Boolean titleEnabled = attributes.get("titleEnabled");
        bindings.put("titleEnabled", titleEnabled);

        Optional<PageStatisticsResponse> pageStatisticsResponse = pageStatisticsWebService.findById(page.id());
        if (pageStatisticsResponse.isPresent()) {
            bindings.put("statistics", pageStatisticsResponse.get());
        } else {
            PageStatisticsResponse statisticsResponse = new PageStatisticsResponse();
            statisticsResponse.totalVisited = 0;
            statisticsResponse.totalCommented = 0;
            bindings.put("statistics", statisticsResponse);
        }

        if (page.content() != null) {
            Node document = parser.parse(page.content());
            String htmlContent = renderer().render(document);
            bindings.put("content", htmlContent);
        }
        template().output(bindings, out);
    }
}
