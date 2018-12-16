package app.jweb.page.api.component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SavedComponentResponse {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "componentName")
    public String componentName;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "status")
    public SavedComponentStatus status;

    @XmlElement(name = "attributes")
    public Map<String, Object> attributes;

    @XmlElement(name = "createdBy")
    public String createdBy;

    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;

    @XmlElement(name = "updatedBy")
    public String updatedBy;

    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
}
