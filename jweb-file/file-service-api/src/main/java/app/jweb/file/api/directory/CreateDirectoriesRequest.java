package app.jweb.file.api.directory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateDirectoriesRequest {
    @XmlElement(name = "path")
    public String path;

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

    @XmlElement(name = "requestBy")
    public String requestBy;
}
