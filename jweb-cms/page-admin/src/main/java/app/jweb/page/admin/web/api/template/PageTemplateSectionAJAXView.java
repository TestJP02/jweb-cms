package app.jweb.page.admin.web.api.template;

import app.jweb.page.admin.web.api.component.PageComponentAJAXView;
import app.jweb.page.api.template.PageSectionWidthView;

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
    public List<PageSectionWidthView> widths;
    @XmlElement(name = "wrapper")
    public Boolean wrapper;
}
