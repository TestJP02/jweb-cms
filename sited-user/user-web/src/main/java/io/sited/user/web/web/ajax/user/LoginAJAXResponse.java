package io.sited.user.web.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginAJAXResponse {
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "fromURL")
    public String fromURL;
}
