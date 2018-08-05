package io.sited.page.admin.web.api.template;

import io.sited.page.api.template.TemplateSectionView;
import io.sited.page.api.template.TemplateType;

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
    @NotNull
    @XmlElement(name = "displayName")
    public String displayName;

    @XmlElement(name = "type")
    public TemplateType type;

    @XmlElement(name = "sections")
    public List<TemplateSectionView> sections;
}
