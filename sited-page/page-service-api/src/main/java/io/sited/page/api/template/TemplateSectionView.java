package io.sited.page.api.template;

import io.sited.page.api.page.PageComponentView;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateSectionView {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "components")
    public List<PageComponentView> components;
    @XmlElement(name = "children")
    public List<TemplateSectionView> children;
    @XmlElement(name = "widths")
    public List<TemplateSectionWidthView> widths;
    @XmlElement(name = "wrapper")
    public Boolean wrapper;
}
