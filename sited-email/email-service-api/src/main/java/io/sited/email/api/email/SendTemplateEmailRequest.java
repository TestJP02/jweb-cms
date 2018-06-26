package io.sited.email.api.email;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SendTemplateEmailRequest {
    @NotNull
    @XmlElement(name = "from")
    public String from;
    @NotNull
    @XmlElement(name = "to")
    public List<String> to;
    @NotNull
    @XmlElement(name = "bindings")
    public Map<String, String> bindings;
    @XmlElement(name = "replyTo")
    public String replyTo;
    @NotNull
    @XmlElement(name = "requestBy")
    public String requestBy;
}
