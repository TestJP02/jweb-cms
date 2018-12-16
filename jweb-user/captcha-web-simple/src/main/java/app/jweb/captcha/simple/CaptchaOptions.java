package app.jweb.captcha.simple;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CaptchaOptions {
    @XmlElement(name = "enabled")
    public Boolean enabled = true;

    @XmlElement(name = "length")
    public Integer length = 4;
}
