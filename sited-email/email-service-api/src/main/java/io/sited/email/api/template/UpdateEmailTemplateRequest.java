package io.sited.email.api.template;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateEmailTemplateRequest {
    @NotNull
    @XmlElement(name = "name")
    public String name;
    @NotNull
    @XmlElement(name = "subject")
    public String subject;
    @NotNull
    @XmlElement(name = "content")
    public String content;
    @NotNull
    @XmlElement(name = "requestBy")
    public String requestBy;
}
