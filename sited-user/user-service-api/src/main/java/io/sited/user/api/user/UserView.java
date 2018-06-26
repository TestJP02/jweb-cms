package io.sited.user.api.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserView {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "username")
    public String username;

    @XmlElement(name = "nickname")
    public String nickname;

    @XmlElement(name = "imageURL")
    public String imageURL;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "status")
    public UserStatus status;

    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
}
