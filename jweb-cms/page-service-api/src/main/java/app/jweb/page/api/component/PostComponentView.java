package app.jweb.page.api.component;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostComponentView {
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
