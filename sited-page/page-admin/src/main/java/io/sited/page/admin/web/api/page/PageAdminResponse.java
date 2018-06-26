package io.sited.page.admin.web.api.page;


import io.sited.page.api.page.PageStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageAdminResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "pageId")
    public String pageId;
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
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "keywords")
    public List<String> keywords;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "totalVisited")
    public Integer totalVisited;
    @XmlElement(name = "totalCommented")
    public Integer totalCommented;
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
