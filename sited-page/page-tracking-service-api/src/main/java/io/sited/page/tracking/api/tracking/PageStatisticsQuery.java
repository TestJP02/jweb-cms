package io.sited.page.tracking.api.tracking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageStatisticsQuery {
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "type")
    public PageTrackingType type;
    @XmlElement(name = "startTime")
    public OffsetDateTime startTime;
    @XmlElement(name = "endTime")
    public OffsetDateTime endTime;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = 10;
}
