package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = Integer.MAX_VALUE;
    @XmlElement(name = "desc")
    public Boolean desc;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "status")
    public PageStatus status;
    @XmlElement(name = "startTime")
    public OffsetDateTime startTime;
    @XmlElement(name = "endTime")
    public OffsetDateTime endTime;
}
