package app.jweb.user.api.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetPasswordResponse {
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "code")
    public String code;
}
