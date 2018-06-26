package io.sited.email.api.email;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SendEmailResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "status")
    public SendEmailStatus status;
    @XmlElement(name = "errorMessage")
    public String errorMessage;
    @XmlElement(name = "result")
    public String result;
}
