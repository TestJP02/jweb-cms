package io.sited.user.web.web.ajax.user;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplyPasswordAJAXRequest {
    @NotNull
    @XmlElement(name = "username")
    public String username;
    @NotNull
    @XmlElement(name = "pinCode")
    public String pinCode;
    @NotNull
    @XmlElement(name = "newPassword")
    public String newPassword;
}
