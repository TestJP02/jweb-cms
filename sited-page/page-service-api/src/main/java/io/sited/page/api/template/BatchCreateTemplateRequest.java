package io.sited.page.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchCreateTemplateRequest {
    @XmlElement(name = "templates")
    public List<TemplateView> templates;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TemplateView {
        @XmlElement(name = "path")
        public String path;
        @XmlElement(name = "type")
        public TemplateType type;
        @XmlElement(name = "displayName")
        public String displayName;
        @XmlElement(name = "sections")
        public List<TemplateSectionView> sections;
        @XmlElement(name = "requestBy")
        public String requestBy;
    }
}
