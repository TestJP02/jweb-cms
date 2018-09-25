package app.jweb.template.impl.processor;

import app.jweb.template.Attribute;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import com.google.common.base.Strings;
import app.jweb.resource.Resource;

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
            String defaultClass = attribute.defaultValue();
            classAttribute.setValue('(' + attribute.value() + ")==null?\'\':(" + attribute.value() + ") + \'" + (Strings.isNullOrEmpty(defaultClass) ? "" : ' ' + defaultClass) + '\'');
            element.addAttribute(classAttribute);
        }
        return element;
    }
}
