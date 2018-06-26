package io.sited.page.api.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePageTagRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "totalTagged")
    public Integer totalTagged;
    @XmlElement(name = "status")
    public PageTagStatus status;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
