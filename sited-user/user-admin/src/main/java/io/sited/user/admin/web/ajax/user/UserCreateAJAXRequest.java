package io.sited.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserCreateAJAXRequest {
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "type")
    public String type;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "userGroupIds")
    public List<String> userGroupIds;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "tags")
    public List<String> tags;
}
