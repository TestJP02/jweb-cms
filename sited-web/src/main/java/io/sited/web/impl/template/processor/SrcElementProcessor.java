package io.sited.web.impl.template.processor;

import io.sited.ApplicationException;
import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;
import io.sited.web.WebRoot;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author chi
 */
public class SrcElementProcessor implements ElementProcessor {
    private final WebRoot webRoot;

    public SrcElementProcessor(WebRoot webRoot) {
        this.webRoot = webRoot;
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
        if (webRoot.get(path).isPresent()) {
            if (isCDNEnabled(element)) {
                Attribute cdnAttribute = new Attribute(attribute.name(), true, attribute.row(), attribute.column(), attribute.source());
                cdnAttribute.setValue("cdn('" + path + "')");
                element.addAttribute(cdnAttribute);
            } else {
                attribute.setValue(path);
            }
        }
        return element;
    }

    private String normalize(Resource template, String src) {
        try {
            String path = new URI(template.path()).resolve(src).normalize().getPath();
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
