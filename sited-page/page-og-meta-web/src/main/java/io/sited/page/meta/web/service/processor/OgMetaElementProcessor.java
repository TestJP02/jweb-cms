package io.sited.page.meta.web.service.processor;

import io.sited.resource.Resource;
import io.sited.template.Attribute;
import io.sited.template.Element;
import io.sited.template.ElementProcessor;


/**
 * @author chi
 */
public class OgMetaElementProcessor implements ElementProcessor {
    @Override
    public Element process(Element element, Resource resource) {
        if ("head".equals(element.name()) && !resource.path().startsWith("admin/")) {
            element.addChild(meta("og:type", "page.fields==null?'article':page.fields.type", element));
            element.addChild(meta("og:locale", "app.language()", element));
            element.addChild(meta("og:url", "request.uri().toString()", element));
            element.addChild(meta("og:title", "page.title", element));
            element.addChild(meta("og:description", "page.description", element));
            element.addChild(meta("og:image", "page.imageURL", element));
        }
        return element;
    }

    private Element meta(String name, String value, Element parent) {
        Element element = new Element("meta", false, parent.row(), parent.column(), parent.source());
        Attribute property = new Attribute("property", false, parent.row(), parent.column(), parent.source());
        property.setValue(name);
        element.addAttribute(property);

        Attribute content = new Attribute("content", true, parent.row(), parent.column(), parent.source());
        content.setValue(value);
        element.addAttribute(content);
        return element;
    }
}
