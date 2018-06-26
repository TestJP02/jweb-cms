package io.sited.user.web.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateEmailAJAXRequest {
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "pinCode")
    public String pinCode;
}
