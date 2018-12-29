package app.jweb.user.web.web.ajax.user;

import app.jweb.user.api.user.UserStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "status")
    public UserStatus status;
}
