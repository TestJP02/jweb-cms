package app.jweb.page.admin.web.api.variable;

import app.jweb.page.api.variable.VariableStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FindVariableAJAXRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "status")
    public VariableStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
