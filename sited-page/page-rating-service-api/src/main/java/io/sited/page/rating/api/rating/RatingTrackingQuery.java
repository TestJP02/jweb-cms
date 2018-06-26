package io.sited.page.rating.api.rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingTrackingQuery {
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "startTime")
    public OffsetDateTime startTime;
    @XmlElement(name = "endTime")
    public OffsetDateTime endTime;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
