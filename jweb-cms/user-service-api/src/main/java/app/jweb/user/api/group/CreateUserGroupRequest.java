package app.jweb.user.api.group;

import app.jweb.user.api.user.UserGroupStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserGroupRequest {
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "name")
    public String name;

    @Size(max = 63)
    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "description")
    public String description;

    @NotNull
    @XmlElement(name = "status")
    public UserGroupStatus status;

    @NotNull
    @XmlElement(name = "roles")
    public List<String> roles;

    @NotNull
    @Size(max = 36)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
