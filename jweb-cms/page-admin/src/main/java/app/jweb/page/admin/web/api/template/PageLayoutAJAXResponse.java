package app.jweb.page.admin.web.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageLayoutAJAXResponse {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "gridColumns")
    public Integer gridColumns;

    @XmlElement(name = "screenWidths")
    public List<String> screenWidths;
}
