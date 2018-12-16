package app.jweb.page.admin.web.api.variable;

import app.jweb.page.api.variable.VariableFieldView;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateVariableAJAXRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "fields")
    public List<VariableFieldView> fields;
}
