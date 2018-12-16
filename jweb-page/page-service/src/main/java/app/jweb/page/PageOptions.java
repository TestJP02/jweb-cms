package app.jweb.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageOptions {
    @XmlElement(name = "auditEnabled")
    public Boolean auditEnabled = false;

    @XmlElement(name = "visitRate")
    public Integer visitRate;
}
