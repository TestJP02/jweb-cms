package app.jweb.page.api.page;

import app.jweb.page.api.component.PostComponentView;

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
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "type")
    public PageType type;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElementWrapper(name = "sections")
    @XmlElement(name = "section")
    public List<PageSectionView> sections;
    @XmlElement(name = "components")
    public Map<String, PostComponentView> components;
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
