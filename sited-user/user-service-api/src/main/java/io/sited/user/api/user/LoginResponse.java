package io.sited.user.api.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginResponse {
    @XmlElement(name = "user")
    public UserResponse user;
    @XmlElement(name = "autoLoginToken")
    public String autoLoginToken;
}
