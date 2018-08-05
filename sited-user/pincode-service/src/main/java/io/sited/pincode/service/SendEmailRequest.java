package io.sited.pincode.service;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SendEmailRequest {
    @NotNull
    @XmlElement(name = "to")
    public String to;
    @NotNull
    @XmlElement(name = "subject")
    public String subject;
    @XmlElement(name = "mimeType")
    public MimeType mimeType;
    @NotNull
    @XmlElement(name = "content")
    public String content;
}
