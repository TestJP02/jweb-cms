package io.sited.user.api.user;

import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
public class UserRegisterMessage {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "userGroupIds")
    public List<String> userGroupIds;
    @XmlElement(name = "status")
    public UserStatus status;
    @XmlElement(name = "type")
    public String type;
    @XmlElement(name = "language")
    public String language;
    @XmlElement(name = "gender")
    public Gender gender;
    @XmlElement(name = "country")
    public String country;
    @XmlElement(name = "state")
    public String state;
    @XmlElement(name = "city")
    public String city;
    @XmlElement(name = "channel")
    public String channel;
    @XmlElement(name = "campaign")
    public String campaign;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
