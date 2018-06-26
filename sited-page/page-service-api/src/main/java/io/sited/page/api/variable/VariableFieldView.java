package io.sited.page.api.variable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VariableFieldView {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "type")
    public VariableFieldType type;
    @XmlElement(name = "value")
    public String value;
}
