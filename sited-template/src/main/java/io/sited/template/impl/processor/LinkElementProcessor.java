package io.sited.template.impl.processor;

import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;

import java.net.URI;
import java.util.Optional;

/**
 * @author chi
 */
public class LinkElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if (isLink(element)) {
            Optional<Attribute> attributeOptional = element.attribute("href");
            if (attributeOptional.isPresent() && !attributeOptional.get().isDynamic() && isRelativePath(attributeOptional.get().value())) {
                Attribute attribute = attributeOptional.get();
                attribute.setValue("/" + URI.create(resource.path()).resolve(attribute.value()).normalize().toString());
            }
        }
        return element;
    }

    private boolean isRelativePath(String path) {
        return !path.startsWith("http://")
            && !path.startsWith("https://")
            && !(path.length() > 0 && path.charAt(0) == '/');
    }

    private boolean isLink(Element element) {
        return "link".equals(element.name());
    }
}
