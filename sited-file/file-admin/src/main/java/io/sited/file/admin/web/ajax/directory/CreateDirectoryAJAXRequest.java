package io.sited.file.admin.web.ajax.directory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateDirectoryAJAXRequest {
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "description")
    public String description;
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
}
