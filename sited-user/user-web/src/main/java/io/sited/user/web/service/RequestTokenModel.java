package io.sited.user.web.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestTokenModel {
    @XmlElement(name = "rawResponse")
    public String rawResponse;
    @XmlElement(name = "token")
    public String token;
    @XmlElement(name = "tokenSecret")
    public String tokenSecret;
    @XmlElement(name = "oauthCallbackConfirmed")
    public Boolean oauthCallbackConfirmed;
    @XmlElement(name = "empty")
    public Boolean empty;
}
