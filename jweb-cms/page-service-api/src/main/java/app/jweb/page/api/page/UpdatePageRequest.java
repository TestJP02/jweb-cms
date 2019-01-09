package app.jweb.page.api.page;

import app.jweb.page.api.template.PageSectionView;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePageRequest {
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "keywords")
    public List<String> keywords;
    @XmlElement(name = "sections")
    public List<PageSectionView> sections;
    @XmlElement(name = "status")
    public PageStatus status;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
