package app.jweb.page.admin.web.api.template;

import app.jweb.page.api.template.TemplateSectionView;
import app.jweb.page.api.template.TemplateType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePageTemplateAJAXRequest {
    @XmlElement(name = "templatePath")
    public String templatePath;

    @XmlElement(name = "path")
    public String path;

    @NotNull
    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "tags")
    public List<String> tags;

    @XmlElement(name = "type")
    public TemplateType type;

    @XmlElement(name = "sections")
    public List<TemplateSectionView> sections;

}
