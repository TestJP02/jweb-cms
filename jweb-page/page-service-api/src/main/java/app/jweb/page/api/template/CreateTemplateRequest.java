package app.jweb.page.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateTemplateRequest {
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "type")
    public TemplateType type;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "sections")
    public List<TemplateSectionView> sections;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
