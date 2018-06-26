package io.sited.page.api.component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateSavedComponentRequest {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "componentName")
    public String componentName;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "attributes")
    public Map<String, Object> attributes;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
