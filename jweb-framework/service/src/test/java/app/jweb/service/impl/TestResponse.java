package app.jweb.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
}
