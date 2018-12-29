package app.jweb.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangePasswordAJAXRequest {
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "password")
    public String password;
}
