package app.jweb.user.admin.web.ajax.group;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserGroupAJAXRequest {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "roles")
    public List<String> roles;
}
