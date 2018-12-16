package app.jweb.file.admin.web.api.directory;

import app.jweb.file.api.directory.DirectoryStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectoryNodeAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "status")
    public DirectoryStatus status;
    @XmlElement(name = "children")
    public List<DirectoryNodeAJAXResponse> children;
    @XmlElement(name = "ownerId")
    public String ownerId;
    @XmlElement(name = "ownerRoles")
    public List<String> ownerRoles;
    @XmlElement(name = "groupId")
    public String groupId;
    @XmlElement(name = "groupRoles")
    public List<String> groupRoles;
    @XmlElement(name = "othersRoles")
    public List<String> othersRoles;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
}
