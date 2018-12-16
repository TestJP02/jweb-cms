package app.jweb.page.meta.web.service.processor;

import app.jweb.resource.Resource;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;


/**
 * @author chi
 */
public class OgMetaElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if ("head".equals(element.name()) && resource.path().startsWith("template/")) {
            Element meta = new Element("og-meta", true, element.row(), element.column(), element.source());
            element.addChild(meta);
        }
        return element;
    }
}
