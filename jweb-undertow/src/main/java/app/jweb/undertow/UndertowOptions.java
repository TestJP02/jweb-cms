package app.jweb.undertow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UndertowOptions {
    @XmlElement(name = "host")
    public String host = "localhost";
    @XmlElement(name = "port")
    public Integer port = 8080;
    @XmlElement(name = "maxEntitySize")
    public Long maxEntitySize = 10000000L;
}
