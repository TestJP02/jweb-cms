package app.jweb.template.impl.processor;

import app.jweb.resource.Resource;
import app.jweb.template.Attribute;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import com.google.common.collect.Lists;

import java.util.Optional;

/**
 * @author chi
 */
public class InnerHtmlElementProcessorImpl implements ElementProcessor {
    private static final String HTML = "innerHtml";

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public Element process(Element element, Resource resource) {
        Optional<Attribute> attributeOptional = element.attribute(HTML);
        if (!element.name().equalsIgnoreCase(HTML) && attributeOptional.isPresent() && attributeOptional.get().isDynamic()) {
            Attribute attribute = attributeOptional.get();
            Element htmlElement = new Element(HTML, true, attribute.row(), attribute.column(), attribute.source());
            htmlElement.setParent(element);
            Attribute content = new Attribute("content", true, attribute.row(), attribute.column(), attribute.source());
            content.setValue(attribute.value());
            htmlElement.addAttribute(content);
            htmlElement.setChildren(element.children());

            element.deleteAttribute(HTML);
            element.setChildren(Lists.newArrayList(htmlElement));
        }
        return element;
    }
}
