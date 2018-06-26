package io.sited.page.tracking.api.tracking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageStatisticsResponse {
    @XmlElement(name = "timestamp")
    public String timestamp;

    @XmlElement(name = "total")
    public Integer total;
}
