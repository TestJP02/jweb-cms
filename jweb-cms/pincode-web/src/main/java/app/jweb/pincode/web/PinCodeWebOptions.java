package app.jweb.pincode.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PinCodeWebOptions {
    @XmlElement(name = "pinCodeEnabled")
    public Boolean pinCodeEnabled = true;
}
