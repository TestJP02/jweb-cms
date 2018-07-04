package io.sited.page.archive.api.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageArchiveQuery {
    @XmlElement(name = "startTime")
    public OffsetDateTime startTime;
    @XmlElement(name = "endTime")
    public OffsetDateTime endTime;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
