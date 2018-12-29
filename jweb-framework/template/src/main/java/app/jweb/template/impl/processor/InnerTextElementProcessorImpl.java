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
public class InnerTextElementProcessorImpl implements ElementProcessor {
    private static final String INNER_TEXT = "innerText";

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public Element process(Element element, Resource resource) {
        Optional<Attribute> attributeOptional = element.attribute(INNER_TEXT);
        if (!element.name().equalsIgnoreCase(INNER_TEXT) && attributeOptional.isPresent() && attributeOptional.get().isDynamic()) {
            Attribute attribute = attributeOptional.get();

            Element textElement = new Element(INNER_TEXT, true, attribute.row(), attribute.column(), attribute.source());
            textElement.setParent(element);

            Attribute content = new Attribute("content", true, attribute.row(), attribute.column(), attribute.source());
            content.setValue(attribute.value());
            textElement.addAttribute(content);

            element.deleteAttribute(INNER_TEXT);
            element.setChildren(Lists.newArrayList(textElement));
        }
        return element;
    }
}
