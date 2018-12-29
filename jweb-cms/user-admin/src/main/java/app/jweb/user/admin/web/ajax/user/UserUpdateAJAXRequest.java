package app.jweb.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserUpdateAJAXRequest {
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "phone")
    public String phone;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "userGroupIds")
    public List<String> userGroupIds;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "tags")
    public List<String> tags;
}
