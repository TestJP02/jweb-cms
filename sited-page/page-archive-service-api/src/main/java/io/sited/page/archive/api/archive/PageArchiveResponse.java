package io.sited.page.archive.api.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageArchiveResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "timestamp")
    public OffsetDateTime timestamp;
    @XmlElement(name = "count")
    public Integer count;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
