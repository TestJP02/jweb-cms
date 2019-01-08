package app.jweb.page.web.service;

import app.jweb.ApplicationException;
import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.component.SavedComponentResponse;
import app.jweb.page.api.template.PageComponentView;
import app.jweb.page.api.template.PageSectionView;
import app.jweb.page.api.template.PageSectionWidthView;
import app.jweb.page.api.template.PageTemplateResponse;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.resource.StringResource;
import app.jweb.template.Component;
import app.jweb.template.TemplateEngine;
import app.jweb.web.WebRoot;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author chi
 */
public class PageTemplateRepository implements ResourceRepository {
    private static final String TEMPLATE_PATH = "template/layout.html";

    @Inject
    PageTemplateWebService pageTemplateWebService;
    @Inject
    TemplateEngine templateEngine;
    @Inject
    PageSavedComponentWebService pageSavedComponentWebService;
    @Inject
    WebRoot webRoot;

    private static final String PREFIX = "/__page/";
    private static final String SUFFIX = ".html";

    public static boolean isPageTemplate(String path) {
        return path.startsWith(PREFIX) && path.endsWith(SUFFIX);
    }

    public static String path(String pageId) {
        return PREFIX + pageId + SUFFIX;
    }

    public static String pageId(String path) {
        return path.substring(PREFIX.length(), path.length() - SUFFIX.length());
    }

    @Override
    public Optional<Resource> get(String path) {
        if (isPageTemplate(path)) {
            String pageId = pageId(path);
            PageTemplateResponse template = pageTemplateWebService.get(pageId);
            StringBuilder b = new StringBuilder();
            for (PageSectionView section : template.sections) {
                appendSection(section, b, 0);
            }
            Resource resource = webRoot.get(TEMPLATE_PATH).orElseThrow(() -> new ApplicationException("missing template, path={}", TEMPLATE_PATH));
            String html = resource.toText(Charsets.UTF_8);
            html = html.replaceAll("<j:layout/>", b.toString());
            return Optional.of(new StringResource(path(template.pageId), html));
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> list(String directory) {
        return ImmutableList.of();
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    private void appendSection(PageSectionView section, StringBuilder b, int depth) {
        if (hasComponents(section)) {
            if (depth == 0) {
                b.append("<div class=\"container");
                if (!Strings.isNullOrEmpty(section.name)) {
                    b.append(" container-").append(section.name);
                }
                b.append("\">");
            }
            if (section.components != null) {
                for (PageComponentView component : section.components) {
                    appendComponent(component, b);
                }
            }
            if (depth == 0) {
                b.append("</div>");
            }
        } else {
            if (depth == 0) {
                b.append("<div class=\"container");
                if (!Strings.isNullOrEmpty(section.name)) {
                    b.append(" container-").append(section.name);
                }
                b.append("\">");
            }
            boolean gridNeeded = isGridNeeded(section.children, section.widths);
            if (gridNeeded) {
                b.append("<div class=\"row\">");
            }
            for (PageSectionView child : section.children) {
                if (gridNeeded) {
                    b.append("<div class=\"").append(classNames(child.widths, section.widths)).append("\">");
                }
                appendSection(child, b, depth + 1);
                if (gridNeeded) {
                    b.append("</div>");
                }
            }
            if (gridNeeded) {
                b.append("</div>");
            }
            if (depth == 0) {
                b.append("</div>");
            }
        }
    }

    private boolean hasComponents(PageSectionView pageSectionView) {
        return pageSectionView.children == null || pageSectionView.children.isEmpty();
    }

    private boolean isGridNeeded(List<PageSectionView> sections, List<PageSectionWidthView> parentWidths) {
        for (PageSectionView section : sections) {
            if (!isSameWidth(section.widths, parentWidths)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameWidth(List<PageSectionWidthView> widths, List<PageSectionWidthView> parentWidths) {
        for (int i = 0; i < widths.size(); i++) {
            PageSectionWidthView width = widths.get(i);
            PageSectionWidthView parentWidth = parentWidths.get(i);
            if (!Objects.equals(width.width, parentWidth.width)) {
                return false;
            }
        }
        return true;
    }

    private String classNames(List<PageSectionWidthView> widths, List<PageSectionWidthView> parentWidths) {
        if (widths == null) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < widths.size(); i++) {
            PageSectionWidthView width = widths.get(i);

            if (width.width != null) {
                PageSectionWidthView parentWidth = parentWidths.get(i);
                int columns = columns(width.width, parentWidth.width);
                switch (width.screenWidth) {
                    case "xs":
                        b.append("col-xs-").append(grid(columns));
                        break;
                    case "sm":
                        b.append("col-sm-").append(grid(columns));
                        break;
                    case "md":
                        b.append("col-md-").append(grid(columns));
                        break;
                    case "lg":
                        b.append("col-lg-").append(grid(columns));
                        break;
                    default:
                        break;
                }
                b.append(' ');
            }

        }
        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    private int columns(int width, int parentWidth) {
        return (int) ((double) width / parentWidth * 12);
    }

    private String grid(int width) {
        return String.valueOf(width);
    }

    @SuppressWarnings({"checkstyle:NestedIfDepth", "PMD.ConsecutiveLiteralAppends"})
    private void appendComponent(PageComponentView pageComponentView, StringBuilder b) {
        Optional<Component> pageComponentOptional = templateEngine.component(pageComponentView.name);
        if (pageComponentOptional.isEmpty()) {
            Optional<SavedComponentResponse> savedComponentResponseOptional = pageSavedComponentWebService.findByName(pageComponentView.name);
            if (savedComponentResponseOptional.isEmpty()) {
                return;
            }
            pageComponentOptional = templateEngine.component(savedComponentResponseOptional.get().componentName);
            if (pageComponentOptional.isEmpty()) {
                return;
            }
        }
        Component pageComponent = pageComponentOptional.get();
        b.append("<j:").append(pageComponent.name()).append(" j:id=\"'").append(pageComponentView.id).append("'\"");

        pageComponent.attributes().forEach((name, attribute) -> {
            b.append(" j:").append(attribute.name()).append("=\"template.components.get(\'").append(pageComponentView.id).append("\').attributes.").append(attribute.name()).append('\"');
        });
        b.append("></j:").append(pageComponent.name()).append('>');
    }
}
