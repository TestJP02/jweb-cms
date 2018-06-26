package io.sited.file.admin.web.ajax.directory;

import io.sited.file.api.directory.DirectoryStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectoryAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "status")
    public DirectoryStatus status;
    @XmlElement(name = "ownerId")
    public String ownerId;
    @XmlElement(name = "groupId")
    public String groupId;
    @XmlElement(name = "groupRoles")
    public List<String> groupRoles;
    @XmlElement(name = "ownerRoles")
    public List<String> ownerRoles;
    @XmlElement(name = "othersRoles")
    public List<String> othersRoles;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
