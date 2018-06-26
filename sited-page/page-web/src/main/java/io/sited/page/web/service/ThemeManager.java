package io.sited.page.web.service;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import io.sited.ApplicationException;
import io.sited.page.api.PageSavedComponentWebService;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.page.api.page.PageComponentView;
import io.sited.page.api.template.TemplateResponse;
import io.sited.page.api.template.TemplateSectionView;
import io.sited.page.api.template.TemplateSectionWidthView;
import io.sited.resource.Resource;
import io.sited.resource.StringResource;
import io.sited.template.Component;
import io.sited.template.TemplateEngine;
import io.sited.web.WebRoot;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author chi
 */
public class ThemeManager {
    private static final String TEMPLATE_PATH = "template/layout.html";
    @Inject
    TemplateEngine templateEngine;
    @Inject
    PageSavedComponentWebService pageSavedComponentWebService;

    @Inject
    WebRoot webRoot;

    public Resource template(TemplateResponse template) {
        StringBuilder b = new StringBuilder();
        for (TemplateSectionView section : template.sections) {
            appendSection(section, b, 0);
        }
        Resource resource = webRoot.get(TEMPLATE_PATH).orElseThrow(() -> new ApplicationException("missing template, path={}", TEMPLATE_PATH));
        String html = resource.toText(Charsets.UTF_8);
        html = html.replaceAll("<j:layout/>", b.toString());
        return new StringResource(template.path, html, template.updatedTime.toInstant().getEpochSecond());
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    private void appendSection(TemplateSectionView section, StringBuilder b, int depth) {
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
            for (TemplateSectionView child : section.children) {
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

    private boolean hasComponents(TemplateSectionView templateSectionView) {
        return templateSectionView.children == null || templateSectionView.children.isEmpty();
    }

    private boolean isGridNeeded(List<TemplateSectionView> sections, List<TemplateSectionWidthView> parentWidths) {
        for (TemplateSectionView section : sections) {
            if (!isSameWidth(section.widths, parentWidths)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameWidth(List<TemplateSectionWidthView> widths, List<TemplateSectionWidthView> parentWidths) {
        for (int i = 0; i < widths.size(); i++) {
            TemplateSectionWidthView width = widths.get(i);
            TemplateSectionWidthView parentWidth = parentWidths.get(i);
            if (!Objects.equals(width.width, parentWidth.width)) {
                return false;
            }
        }
        return true;
    }

    private String classNames(List<TemplateSectionWidthView> widths, List<TemplateSectionWidthView> parentWidths) {
        if (widths == null) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < widths.size(); i++) {
            TemplateSectionWidthView width = widths.get(i);

            if (width.width != null) {
                TemplateSectionWidthView parentWidth = parentWidths.get(i);
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
        return (int) ((double) width / parentWidth * 24);
    }

    private String grid(int width) {
        return String.valueOf(width);
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    private void appendComponent(PageComponentView pageComponentView, StringBuilder b) {
        Optional<Component> pageComponentOptional = templateEngine.component(pageComponentView.name);
        if (!pageComponentOptional.isPresent()) {
            Optional<SavedComponentResponse> savedComponentResponseOptional = pageSavedComponentWebService.findByName(pageComponentView.name);
            if (!savedComponentResponseOptional.isPresent()) {
                return;
            }
            pageComponentOptional = templateEngine.component(savedComponentResponseOptional.get().componentName);
            if (!pageComponentOptional.isPresent()) {
                return;
            }
        }
        Component pageComponent = pageComponentOptional.get();
        b.append("<j:");
        b.append(pageComponent.name());
        b.append(" j:id=\"'").append(pageComponentView.id).append("'\"");

        pageComponent.attributes().forEach(attribute -> {
            b.append(" j:");
            b.append(attribute.name());
            b.append("=\"template.components.get(\'").append(pageComponentView.id).append("\').attributes.").append(attribute.name()).append('\"');
        });
        b.append("></j:").append(pageComponent.name()).append('>');
    }
}
