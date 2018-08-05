package io.sited.page.meta.web.service.processor;

import io.sited.resource.Resource;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;


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
