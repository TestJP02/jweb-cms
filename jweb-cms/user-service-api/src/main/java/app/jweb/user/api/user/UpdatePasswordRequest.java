package app.jweb.user.api.user;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePasswordRequest {
    @NotNull
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
