package app.jweb.web.impl.processor;

import app.jweb.ApplicationException;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.template.Attribute;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import app.jweb.web.impl.ThemedResource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class SrcElementProcessor implements ElementProcessor {
    private final List<String> cdnBaseURLs;
    private final ResourceRepository repository;

    public SrcElementProcessor(List<String> cdnBaseURLs, ResourceRepository repository) {
        this.cdnBaseURLs = cdnBaseURLs;
        this.repository = repository;
    }

    @Override
    public Element process(Element element, Resource resource) {
        if (!isScriptNode(element) && !isImageNode(element)) {
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
        if (repository.get(path).isPresent()) {
            if (isCDNEnabled(resource)) {
                attribute.setValue(cdnURL(path));
            } else {
                attribute.setValue('/' + path);
            }
        }
        return element;
    }

    private String cdnURL(String path) {
        String baseURL = cdnBaseURLs.get(Math.abs(path.hashCode() % cdnBaseURLs.size()));
        return baseURL + path;
    }

    private String normalize(Resource resource, String src) {
        try {
            if (resource instanceof ThemedResource) {
                ThemedResource themedResource = (ThemedResource) resource;
                return new URI(themedResource.raw().path()).resolve(src).normalize().getPath();
            }
            return new URI(resource.path()).resolve(src).normalize().getPath();
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

    private boolean isImageNode(Element node) {
        return "img".equalsIgnoreCase(node.name());
    }

    private boolean isCDNEnabled(Resource resource) {
        return cdnBaseURLs != null && !cdnBaseURLs.isEmpty() && resource.path().startsWith("template/");
    }
}
