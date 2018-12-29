package app.jweb.user.web.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfoResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "roles")
    public List<String> roles;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "authenticated")
    public boolean authenticated;
}
