package io.sited.page.admin.web.api.template;

import io.sited.page.admin.web.api.page.PageComponentAJAXView;
import io.sited.page.api.template.TemplateSectionWidthView;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTemplateSectionAJAXView {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "components")
    public List<PageComponentAJAXView> components;
    @XmlElement(name = "children")
    public List<PageTemplateSectionAJAXView> children;
    @XmlElement(name = "widths")
    public List<TemplateSectionWidthView> widths;
    @XmlElement(name = "wrapper")
    public Boolean wrapper;
}
