package io.sited.page.web.service.processor;

import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;
import io.sited.template.Node;

import java.util.Optional;

/**
 * @author chi
 */
public class ThemeProcessor implements ElementProcessor {
    private final String themeName;

    public ThemeProcessor(String themeName) {
        this.themeName = themeName;
    }

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public Element process(Element element, Resource resource) {
        if (isTemplate(resource) && isThemeCss(element)) {
            Element themeElement = new Element("theme-css", true, element.row(), element.column(), element.source());
            Attribute attribute = new Attribute("name", false, element.row(), element.column(), element.source());
            attribute.setValue(themeName);
            themeElement.addAttribute(attribute);
            element.parent().replaceChild(element, themeElement);

            Optional<Element> body = body(element);
            if (body.isPresent()) {
                Element themeScriptElement = new Element("theme-script", true, element.row(), element.column(), element.source());
                Attribute themeScriptNameAttribute = new Attribute("name", false, element.row(), element.column(), element.source());
                themeScriptNameAttribute.setValue(themeName);
                themeScriptElement.addAttribute(themeScriptNameAttribute);
                body.get().addChild(themeScriptElement);
            }
        }
        return element;
    }

    private Optional<Element> html(Element element) {
        Element current = element;
        while (current != null) {
            if (current.name().equals("html")) {
                return Optional.of(current);
            }
            current = current.parent();
        }
        return Optional.empty();
    }

    private Optional<Element> body(Element element) {
        Optional<Element> html = html(element);
        if (html.isPresent()) {
            for (Node child : html.get().children()) {
                if (child.name().equals("body")) {
                    return Optional.of((Element) child);
                }
            }
        }
        return Optional.empty();
    }

    private boolean isThemeCss(Element element) {
        if (!isLinkElement(element)) {
            return false;
        }
        Optional<Attribute> href = element.attribute("href");
        return href.isPresent() && href.get().value().contains("bootstrap.min.");
    }

    private boolean isTemplate(Resource resource) {
        return resource.path().startsWith("template/");
    }

    private boolean isLinkElement(Element element) {
        return "link".equalsIgnoreCase(element.name());
    }
}
