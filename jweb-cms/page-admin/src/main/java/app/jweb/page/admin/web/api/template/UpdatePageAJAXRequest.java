package app.jweb.page.admin.web.api.template;

import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.template.PageSectionView;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePageAJAXRequest {
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @NotNull
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
}
