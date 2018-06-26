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
public class ScriptElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if (isScript(element)) {
            Optional<Attribute> attributeOptional = element.attribute("src");
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

    private boolean isScript(Element element) {
        return "script".equals(element.name());
    }
}
