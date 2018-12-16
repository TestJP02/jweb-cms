package app.jweb.page.tracking.baidu.service.processor;

import app.jweb.resource.Resource;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;


/**
 * @author chi
 */
public class BaiduTrackingElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if ("body".equals(element.name()) && resource.path().startsWith("template/")) {
            Element component = new Element("baidu-tracking", true, element.row(), element.column(), element.source());
            element.addChild(component);
        }
        return element;
    }
}
