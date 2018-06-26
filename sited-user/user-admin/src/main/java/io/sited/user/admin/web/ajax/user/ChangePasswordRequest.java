package io.sited.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangePasswordRequest {
    @XmlElement(name = "user")
    public String user;
    @XmlElement(name = "oldPassword")
    public String oldPassword;
    @XmlElement(name = "newPassword")
    public String newPassword;
}
