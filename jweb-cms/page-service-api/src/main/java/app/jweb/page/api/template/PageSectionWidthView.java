package app.jweb.page.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageSectionWidthView {
    @XmlElement(name = "screenWidth")
    public String screenWidth;
    @XmlElement(name = "x")
    public Integer x;
    @XmlElement(name = "y")
    public Integer y;
    @XmlElement(name = "width")
    public Integer width;
    @XmlElement(name = "height")
    public Integer height;
}
