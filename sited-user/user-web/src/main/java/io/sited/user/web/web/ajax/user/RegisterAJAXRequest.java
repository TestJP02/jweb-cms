package io.sited.user.web.web.ajax.user;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterAJAXRequest {
    @XmlElement(name = "userId")
    public String userId;

    @XmlElement(name = "username")
    public String username;

    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "pinCode")
    public String pinCode;
}
