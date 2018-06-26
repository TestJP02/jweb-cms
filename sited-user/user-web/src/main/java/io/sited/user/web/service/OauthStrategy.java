package io.sited.user.web.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OauthStrategy {
    @XmlElement(name = "clientId")
    public String clientId;
    @XmlElement(name = "clientSecret")
    public String clientSecret;
    @XmlElement(name = "callback")
    public String callback;
}
