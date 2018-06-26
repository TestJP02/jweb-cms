package io.sited.user.web;

import io.sited.user.web.service.OauthStrategy;
import io.sited.user.web.service.UsernameStrategy;
import io.sited.user.web.service.ValidationRules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWebOptions {
    @XmlElement(name = "registerAutoLoginEnabled")
    public Boolean registerAutoLoginEnabled = true;
    @XmlElement(name = "autoLoginEnabled")
    public Boolean autoLoginEnabled = true;
    @XmlElement(name = "autoLoginMaxAge")
    public Integer autoLoginMaxAge = Integer.MAX_VALUE;
    @XmlElement(name = "autoLoginCookie")
    public String autoLoginCookie = "token";
    @XmlElement(name = "userNameStrategy")
    public UsernameStrategy usernameStrategy = UsernameStrategy.EMAIL;
    @XmlElement(name = "google")
    public OauthStrategy google;
    @XmlElement(name = "facebook")
    public OauthStrategy facebook;
    @XmlElement(name = "github")
    public OauthStrategy github;
    @XmlElement(name = "twitter")
    public OauthStrategy twitter;
    @XmlElement(name = "weibo")
    public OauthStrategy weibo;
    @XmlElement(name = "validationRule")
    public ValidationRules validationRules;
}
