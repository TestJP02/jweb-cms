package io.sited.pincode.web.web.ajax;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatePinCodeAJAXRequest {
    @XmlElement(name = "email")
    public String email;

    @XmlElement(name = "phone")
    public String phone;
}
