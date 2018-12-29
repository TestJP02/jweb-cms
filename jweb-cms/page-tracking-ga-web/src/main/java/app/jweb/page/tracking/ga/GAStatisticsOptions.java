package app.jweb.page.tracking.ga;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GAStatisticsOptions {
    @XmlElement(name = "id")
    public String id;
}
