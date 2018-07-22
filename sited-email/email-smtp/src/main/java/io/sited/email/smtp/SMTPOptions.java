package io.sited.email.smtp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SMTPOptions {
    @XmlElement(name = "host")
    public String host;
    @XmlElement(name = "port")
    public Integer port;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "password")
    public String password;
    @XmlElement(name = "ssl")
    public Boolean ssl;
    @XmlElement(name = "starttls")
    public Boolean starttls;
}
