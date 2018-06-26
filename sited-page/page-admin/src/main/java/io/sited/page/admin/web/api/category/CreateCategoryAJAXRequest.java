package io.sited.page.admin.web.api.category;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCategoryAJAXRequest {
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "templatePath")
    @NotNull
    public String templatePath;
    @XmlElement(name = "path")
    @NotNull
    public String path;
    @XmlElement(name = "displayName")
    @NotNull
    public String displayName;

    @XmlElement(name = "displayOrder")
    @NotNull
    public Integer displayOrder;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "keywords")
    @NotNull
    public List<String> keywords;
    @XmlElement(name = "tags")
    @NotNull
    public List<String> tags;
    @XmlElement(name = "imageURL")
    public String imageURL;
    //todo add not null validation
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
}
