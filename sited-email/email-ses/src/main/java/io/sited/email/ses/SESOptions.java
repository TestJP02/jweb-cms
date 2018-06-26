package io.sited.email.ses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SESOptions {
    @XmlElement(name = "accessKey")
    public String accessKey;
    @XmlElement(name = "secretKey")
    public String secretKey;
    @XmlElement(name = "region")
    public String region;
}
