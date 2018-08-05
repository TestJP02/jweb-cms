package io.sited.page.tracking.baidu.service.processor;

import io.sited.resource.Resource;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;


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
