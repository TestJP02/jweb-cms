package io.sited.user.web.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationRule {
    @XmlElement(name = "minLength")
    public Integer minLength;
    @XmlElement(name = "maxLength")
    public Integer maxLength;
    @XmlElement(name = "regex")
    public String regex;
}
