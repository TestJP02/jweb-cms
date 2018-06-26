package io.sited.page.tracking.admin.web.ajax.tracking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTrackingAdminResponse {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "pageId")
    public String pageId;

    @XmlElement(name = "categoryId")
    public String categoryId;

    @XmlElement(name = "totalVisited")
    public Integer totalVisited;

    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;

    @XmlElement(name = "createdBy")
    public String createdBy;

    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "path")
    public String path;

    @XmlElement(name = "timestamp")
    public String timestamp;
}
