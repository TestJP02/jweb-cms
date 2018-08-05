package io.sited.page.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateTemplateRequest {
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElement(name = "type")
    public TemplateType type;
    @XmlElement(name = "sections")
    public List<TemplateSectionView> sections;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
