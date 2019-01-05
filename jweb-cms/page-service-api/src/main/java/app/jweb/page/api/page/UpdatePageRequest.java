package app.jweb.page.api.page;

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
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "type")
    public PageType type;
    @XmlElement(name = "sections")
    public List<PageSectionView> sections;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
