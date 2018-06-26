package io.sited.email.api.email;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SendEmailRequest {
    @NotNull
    @XmlElement(name = "from")
    public String from;
    @NotNull
    @XmlElement(name = "to")
    public List<String> to;
    @NotNull
    @XmlElement(name = "replyTo")
    public String replyTo;
    @NotNull
    @XmlElement(name = "subject")
    public String subject;
    @XmlElement(name = "mimeType")
    public MimeType mimeType;
    @NotNull
    @XmlElement(name = "content")
    public String content;
    @NotNull
    @XmlElement(name = "requestBy")
    public String requestBy;
}
