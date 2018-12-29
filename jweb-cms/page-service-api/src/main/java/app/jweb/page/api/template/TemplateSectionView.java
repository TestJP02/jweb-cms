package app.jweb.page.api.template;

import app.jweb.page.api.component.PostComponentView;

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
    public List<PostComponentView> components;
    @XmlElement(name = "children")
    public List<TemplateSectionView> children;
    @XmlElement(name = "widths")
    public List<TemplateSectionWidthView> widths;
    @XmlElement(name = "wrapper")
    public Boolean wrapper;
}
