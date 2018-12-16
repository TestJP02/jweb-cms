package app.jweb.user.admin;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAdminOptions {
    @XmlElement(name = "autoLoginEnabled")
    public Boolean autoLoginEnabled = true;
    @XmlElement(name = "autoLoginMaxAge")
    public Integer autoLoginMaxAge = Integer.MAX_VALUE;
    @XmlElement(name = "autoLoginCookie")
    public String autoLoginCookie = "token";
}
