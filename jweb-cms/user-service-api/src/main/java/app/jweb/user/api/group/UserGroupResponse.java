package app.jweb.user.api.group;

import app.jweb.user.api.user.UserGroupStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserGroupResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "status")
    public UserGroupStatus status;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "roles")
    public List<String> roles;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
