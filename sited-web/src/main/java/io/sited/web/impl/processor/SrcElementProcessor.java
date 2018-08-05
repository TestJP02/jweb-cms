package io.sited.web.impl.processor;

import io.sited.ApplicationException;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author chi
 */
public class SrcElementProcessor implements ElementProcessor {
    private final ResourceRepository repository;

    public SrcElementProcessor(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Element process(Element element, Resource resource) {
        if (!isScriptNode(element)) {
            return element;
        }
        Optional<Attribute> attributeOptional = element.attribute("src");
        if (!attributeOptional.isPresent() || attributeOptional.get().isDynamic()) {
            return element;
        }

        Attribute attribute = attributeOptional.get();
        String href = attribute.value();
        if (!isRelativeURL(href)) {
            return element;
        }
        String path = normalize(resource, href);
        if (repository.get(path.substring(1)).isPresent()) {
            if (isCDNEnabled(element)) {
                element.deleteAttribute("cdn");

                Attribute cdnAttribute = new Attribute(attribute.name(), true, attribute.row(), attribute.column(), attribute.source());
                cdnAttribute.setValue("cdn('" + path + "')");
                element.addAttribute(cdnAttribute);
            } else {
                attribute.setValue(path);
            }
        }
        return element;
    }

    private String normalize(Resource resource, String src) {
        try {
            String path = new URI(resource.path()).resolve(src).normalize().getPath();
            return path.length() > 0 && path.charAt(0) == '/' ? path : '/' + path;
        } catch (URISyntaxException e) {
            throw new ApplicationException(e);
        }
    }

    private boolean isRelativeURL(String path) {
        return !(path.length() > 0 && path.charAt(0) == '/') && !path.startsWith("http://") && !path.startsWith("https://");
    }

    private boolean isScriptNode(Element node) {
        return "script".equalsIgnoreCase(node.name());
    }

    private boolean isCDNEnabled(Element element) {
        return element.attribute("cdn").isPresent();
    }
}
