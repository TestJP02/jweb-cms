package app.jweb.post.api.category;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCategoryRequest {
    @XmlElement(name = "parentId")
    public String parentId;
    @NotNull
    @XmlElement(name = "templatePath")
    public String templatePath;
    @NotNull
    @XmlElement(name = "path")
    public String path;
    @NotNull
    @XmlElement(name = "displayName")
    public String displayName;
    @NotNull
    @XmlElement(name = "keywords")
    public List<String> keywords;
    @NotNull
    @XmlElement(name = "displayOrder")
    public Integer displayOrder;
    @XmlElement(name = "description")
    public String description;
    @NotNull
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "imageURL")
    public String imageURL;
    //@NotNull
    @XmlElement(name = "ownerId")
    public String ownerId;
    //@NotNull
    //@NotEmpty
    @XmlElement(name = "ownerRoles")
    public List<String> ownerRoles;
    //@NotNull
    @XmlElement(name = "groupId")
    public String groupId;
    //@NotNull
    @NotEmpty
    @XmlElement(name = "groupRoles")
    public List<String> groupRoles;
    //@NotNull
    //@NotEmpty
    @XmlElement(name = "othersRoles")
    public List<String> othersRoles;
    @NotNull
    @XmlElement(name = "requestBy")
    public String requestBy;
}
