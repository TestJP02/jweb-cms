package app.jweb.page.api.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VariableResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "fields")
    public List<VariableFieldView> fields;
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
