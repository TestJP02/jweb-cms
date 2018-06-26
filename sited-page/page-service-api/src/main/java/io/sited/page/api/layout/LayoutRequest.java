package io.sited.page.api.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LayoutRequest {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "screenWidths")
    public List<String> screenWidths;

    @XmlElement(name = "gridColumns")
    public Integer gridColumns;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
