package io.sited.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailTemplateOptions {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "path")
    public String path;
}
