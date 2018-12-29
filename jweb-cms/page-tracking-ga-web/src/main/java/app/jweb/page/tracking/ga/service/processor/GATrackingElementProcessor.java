package app.jweb.page.tracking.ga.service.processor;

import app.jweb.resource.Resource;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;


/**
 * @author chi
 */
public class GATrackingElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if ("body".equals(element.name()) && resource.path().startsWith("template/")) {
            Element component = new Element("ga-tracking", true, element.row(), element.column(), element.source());
            element.addChild(component);
        }
        return element;
    }
}
