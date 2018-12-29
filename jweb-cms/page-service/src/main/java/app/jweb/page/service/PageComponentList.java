package app.jweb.page.service;

import app.jweb.page.api.component.ComponentResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageComponentList {
    @XmlElement(name = "components")
    public List<ComponentResponse> components;
}
