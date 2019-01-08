package app.jweb.page.api.template;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageComponentView {
    @NotNull
    @XmlElement(name = "id")
    public String id;

    @NotNull
    @XmlElement(name = "name")
    public String name;

    @NotNull
    @XmlElement(name = "attributes")
    public Map<String, Object> attributes;
}
