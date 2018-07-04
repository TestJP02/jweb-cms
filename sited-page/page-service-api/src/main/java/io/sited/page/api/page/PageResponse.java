package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "version")
    public Integer version;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    public List<String> tags;
    @XmlElementWrapper(name = "keywords")
    @XmlElement(name = "keyword")
    public List<String> keywords;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "imageURLs")
    public List<String> imageURLs;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
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
