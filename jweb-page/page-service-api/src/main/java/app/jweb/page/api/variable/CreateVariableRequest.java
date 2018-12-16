package app.jweb.page.api.variable;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateVariableRequest {
    @XmlElement(name = "name")
    @NotNull
    @Size(max = 127)
    public String name;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    @NotNull
    public List<VariableFieldView> fields;
    @XmlElement(name = "requestBy")
    @NotNull
    @Size(max = 63)
    public String requestBy;
}
