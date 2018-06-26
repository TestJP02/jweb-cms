package io.sited.page.tracking.api.tracking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTrackingQuery {
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "pageOnly")
    public Boolean pageOnly = true;
    @XmlElement(name = "type")
    public PageTrackingType type = PageTrackingType.DAILY;
    @XmlElement(name = "limit")
    public Integer limit = 10;
}
