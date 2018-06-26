package io.sited.page.api.component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateComponentRequest {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "attributes")
    public Map<String, Object> attributes;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
