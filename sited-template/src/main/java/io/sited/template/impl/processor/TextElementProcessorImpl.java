package io.sited.template.impl.processor;

import com.google.common.collect.Lists;
import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;

import java.util.Optional;

/**
 * @author chi
 */
public class TextElementProcessorImpl implements ElementProcessor {
    private static final String TEXT = "text";

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public Element process(Element element, Resource resource) {
        Optional<Attribute> attributeOptional = element.attribute(TEXT);
        if (!element.name().equalsIgnoreCase(TEXT) && attributeOptional.isPresent() && attributeOptional.get().isDynamic()) {
            Attribute attribute = attributeOptional.get();

            Element textElement = new Element(TEXT, true, attribute.row(), attribute.column(), attribute.source());
            textElement.setParent(element);

            Attribute content = new Attribute("content", true, attribute.row(), attribute.column(), attribute.source());
            content.setValue(attribute.value());
            content.setDefaultValue(element.innerText());
            textElement.addAttribute(content);

            element.deleteAttribute(TEXT);
            element.setChildren(Lists.newArrayList(textElement));
        }
        return element;
    }
}
