package app.jweb.page.admin.web.api.variable;

import app.jweb.page.api.variable.VariableFieldView;
import app.jweb.page.api.variable.VariableStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VariableAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "fields")
    public List<VariableFieldView> fields;
    @XmlElement(name = "fieldNum")
    public Integer fieldNum;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "status")
    public VariableStatus status;
}
