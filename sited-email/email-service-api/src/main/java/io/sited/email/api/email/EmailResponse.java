package io.sited.email.api.email;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "from")
    public String from;
    @XmlElement(name = "replyTo")
    public String replyTo;
    @XmlElement(name = "to")
    public String to;
    @XmlElement(name = "subject")
    public String subject;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "status")
    public SendEmailStatus status;
    @XmlElement(name = "errorMessage")
    public String errorMessage;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
}
