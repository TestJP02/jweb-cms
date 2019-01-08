package app.jweb.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageUpdatedMessage {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "categoryIds")
    public List<String> categoryIds;
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "keywords")
    public List<String> keywords;
    @XmlElement(name = "status")
    public PageStatus status;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
