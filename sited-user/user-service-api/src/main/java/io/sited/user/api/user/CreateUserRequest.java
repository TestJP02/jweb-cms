package io.sited.user.api.user;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @NotNull
    @XmlElement(name = "password")
    public String password;
    @Size(max = 63)
    @XmlElement(name = "email")
    public String email;
    @Size(max = 11)
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "imageURL")
    public String imageURL;
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
    @XmlElement(name = "userGroupIds")
    public List<String> userGroupIds;
    @NotNull
    @XmlElement(name = "status")
    public UserStatus status;
    @XmlElement(name = "description")
    public String description;
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
}
