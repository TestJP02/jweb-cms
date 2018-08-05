package io.sited.page.admin.web.api.template;

import io.sited.page.api.template.TemplateStatus;
import io.sited.page.api.template.TemplateType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTemplateAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElement(name = "sections")
    public List<PageTemplateSectionAJAXView> sections;
    @XmlElement(name = "status")
    public TemplateStatus status;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "type")
    public TemplateType type;
}
