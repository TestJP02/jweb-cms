package io.sited.template.impl.processor;

import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;

import java.util.Optional;

/**
 * @author chi
 */
public class ClassElementProcessorImpl implements ElementProcessor {
    @Override
    public int priority() {
        return 80;
    }

    @Override
    public Element process(Element element, Resource resource) {
        Optional<Attribute> attributeOptional = element.attribute("class");
        if (attributeOptional.isPresent() && attributeOptional.get().isDynamic()) {
            Attribute attribute = attributeOptional.get();
            Attribute classAttribute = new Attribute(attribute.name(), true, attribute.row(), attribute.column(), attribute.source());
            classAttribute.setValue('(' + attribute.value() + ") + \' " + attribute.defaultValue() + '\'');
            element.addAttribute(classAttribute);
        }
        return element;
    }
}
