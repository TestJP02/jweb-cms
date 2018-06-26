package io.sited.page.admin.web.api.component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePageSavedComponentAJAXRequest {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "componentName")
    public String componentName;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "attributes")
    public Map<String, Object> attributes;
}
