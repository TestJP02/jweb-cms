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
}
