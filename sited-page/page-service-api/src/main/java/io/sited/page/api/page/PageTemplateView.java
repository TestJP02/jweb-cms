package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTemplateView {
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElementWrapper(name = "components")
    @XmlElement(name = "component")
    public Map<String, PageComponentView> components;
}
