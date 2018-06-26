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
public class HtmlElementProcessorImpl implements ElementProcessor {
    private static final String HTML = "html";

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
