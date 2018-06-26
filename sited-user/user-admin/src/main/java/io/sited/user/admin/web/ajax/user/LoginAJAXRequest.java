package io.sited.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginAJAXRequest {
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "captchaCode")
    public String captchaCode;
    @XmlElement(name = "autoLogin")
    public Boolean autoLogin;
}
