package io.sited.user.api.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserAutoLoginTokenRequest {
    @XmlElement(name = "userId")
    public String userId;

    @XmlElement(name = "expireTime")
    public OffsetDateTime expireTime;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
