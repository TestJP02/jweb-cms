package app.jweb.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchCreatePageRequest {
    @XmlElement(name = "templates")
    public List<TemplateView> templates;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TemplateView {
        @XmlElement(name = "userId")
        public String userId;
        @XmlElement(name = "path")
        public String path;
        @XmlElement(name = "templatePath")
        public String templatePath;
        @XmlElement(name = "type")
        public PageType type;
        @XmlElement(name = "title")
        public String title;
        @XmlElement(name = "description")
        public String description;
        @XmlElement(name = "tags")
        public List<String> tags;
        @XmlElement(name = "sections")
        public List<PageSectionView> sections;
        @XmlElement(name = "requestBy")
        public String requestBy;
    }
}
