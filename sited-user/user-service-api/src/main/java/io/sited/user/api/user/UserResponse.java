package io.sited.user.api.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
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
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
}
