package app.jweb.user.api.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserAutoLoginTokenResponse {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "userId")
    public String userId;

    @XmlElement(name = "token")
    public String token;

    @XmlElement(name = "expiredTime")
    public OffsetDateTime expiredTime;

    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;

    @XmlElement(name = "createdBy")
    public String createdBy;
}