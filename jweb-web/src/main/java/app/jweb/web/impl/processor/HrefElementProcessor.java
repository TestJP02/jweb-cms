package app.jweb.web.impl.processor;

import app.jweb.ApplicationException;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.template.Attribute;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import app.jweb.template.Text;
import app.jweb.web.impl.ThemedResource;
import com.google.common.base.Charsets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class HrefElementProcessor implements ElementProcessor {
    private final List<String> cdnBaseURLs;
    private final ResourceRepository repository;
    private final boolean inlineEnabled;

    public HrefElementProcessor(List<String> cdnBaseURLs, ResourceRepository repository, boolean inlineEnabled) {
        this.cdnBaseURLs = cdnBaseURLs;
        this.repository = repository;
        this.inlineEnabled = inlineEnabled;
    }

    @Override
    public Element process(Element element, Resource resource) {
        if (!isLinkElement(element)) {
            return element;
        }

        Optional<Attribute> attributeOptional = element.attribute("href");
        if (!attributeOptional.isPresent() || attributeOptional.get().isDynamic()) {
            return element;
        }
        Attribute attribute = attributeOptional.get();
        String href = attribute.value();
        if (!isRelativeURL(href)) {
            return element;
        }
        String path = normalize(resource, href);
        Optional<Resource> style = repository.get(path);
        if (style.isPresent()) {
            if (inlineEnabled) {
                element.deleteAttribute("href");
                Text text = new Text(style.get().toText(Charsets.UTF_8), element.row(), element.column(), element.source());
                element.addChild(text);
            } else if (isCDNEnabled(resource)) {
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
        return !(path.length() > 0 && path.charAt(0) == '/') && !path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("://");
    }

    private boolean isLinkElement(Element element) {
        return "link".equalsIgnoreCase(element.name());
    }

    private boolean isCDNEnabled(Resource resource) {
        return cdnBaseURLs != null && !cdnBaseURLs.isEmpty() && resource.path().startsWith("template/");
    }
}
