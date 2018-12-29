package app.jweb.user.web.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckCaptchaCodeAJAXResponse {
    @XmlElement(name = "valid")
    public Boolean valid;
}
