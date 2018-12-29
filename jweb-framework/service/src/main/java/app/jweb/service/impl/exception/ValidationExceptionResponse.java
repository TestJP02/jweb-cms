package app.jweb.service.impl.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author chi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationExceptionResponse {
    @XmlElement(name = "path")
    public String path;

    @XmlElement(name = "errorMessage")
    public String errorMessage;
}
