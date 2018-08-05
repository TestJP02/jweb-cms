package io.sited.pincode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PinCodeOptions {
    @XmlElement(name = "length")
    public Integer length = 4;

    @XmlElement(name = "dailyRate")
    public Integer dailyRate = 10;

    @XmlElement(name = "smtp")
    public SMTPOptions smtp;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SMTPOptions {
        @XmlElement(name = "host")
        public String host;
        @XmlElement(name = "port")
        public Integer port;
        @XmlElement(name = "username")
        public String username;
        @XmlElement(name = "replyTo")
        public String replyTo;
        @XmlElement(name = "password")
        public String password;
        @XmlElement(name = "sslEnabled")
        public Boolean sslEnabled;
        @XmlElement(name = "starttlsEnabled")
        public Boolean starttlsEnabled;
    }
}
