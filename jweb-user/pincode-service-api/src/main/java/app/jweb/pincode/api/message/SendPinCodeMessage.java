package app.jweb.pincode.api.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SendPinCodeMessage {
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "code")
    public String code;
    @XmlElement(name = "language")
    public String language;
}
