package app.jweb.user.api.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteUserAutoLoginTokenRequest {
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
