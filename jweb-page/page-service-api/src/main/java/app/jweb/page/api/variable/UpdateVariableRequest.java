package app.jweb.page.api.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateVariableRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    public List<VariableFieldView> fields;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
