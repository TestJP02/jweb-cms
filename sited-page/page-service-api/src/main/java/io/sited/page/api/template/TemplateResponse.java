package io.sited.page.api.template;

import io.sited.page.api.page.PageComponentView;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "readOnly")
    public Boolean readOnly;
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElementWrapper(name = "sections")
    @XmlElement(name = "section")
    public List<TemplateSectionView> sections;
    @XmlElement(name = "components")
    public Map<String, PageComponentView> components;
    @XmlElement(name = "status")
    public TemplateStatus status;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
