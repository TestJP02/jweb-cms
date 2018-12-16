package app.jweb.page.tracking.baidu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaiduTrackingOptions {
    @XmlElement(name = "id")
    public String id;
}
