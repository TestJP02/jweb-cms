package io.sited.page.api.category;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryNodeResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "parentIds")
    public Object parentIds;
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElement(name = "keywords")
    public List<String> keywords;
    @XmlElement(name = "displayOrder")
    public Integer displayOrder;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "status")
    public CategoryStatus status;
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
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
    @XmlElement(name = "children")
    public List<CategoryNodeResponse> children;
}
