package io.sited.user.web.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationRules {
    @XmlElement(name = "username")
    public ValidationRule username = new ValidationRule();
    @XmlElement(name = "password")
    public ValidationRule password = new ValidationRule();
    @XmlElement(name = "phone")
    public ValidationRule phone = new ValidationRule();
    @XmlElement(name = "email")
    public ValidationRule email = new ValidationRule();
}
