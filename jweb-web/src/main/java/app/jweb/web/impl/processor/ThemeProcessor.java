package app.jweb.web.impl.processor;

import app.jweb.resource.Resource;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import app.jweb.template.Node;

import java.util.Optional;

/**
 * @author chi
 */
public class ThemeProcessor implements ElementProcessor {
    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public Element process(Element element, Resource resource) {
        if (isTemplate(resource) && isHead(element)) {
            Element themeElement = new Element("theme-css", true, element.row(), element.column(), element.source());
            element.addChild(themeElement);

            Optional<Element> body = body(element);
            if (body.isPresent()) {
                Element themeScriptElement = new Element("theme-script", true, element.row(), element.column(), element.source());
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

    private boolean isTemplate(Resource resource) {
        return resource.path().startsWith("template/");
    }

    private boolean isHead(Element element) {
        return "head".equalsIgnoreCase(element.name());
    }
}
