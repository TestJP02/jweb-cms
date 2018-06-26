package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HottestQuery {
    @XmlElement(name = "limit")
    public Integer limit = 10;
    @XmlElement(name = "startDate")
    public OffsetDateTime startDate;
    @XmlElement(name = "endDate")
    public OffsetDateTime endDate;
}
