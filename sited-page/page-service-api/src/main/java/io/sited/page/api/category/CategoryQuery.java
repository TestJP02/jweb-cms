package io.sited.page.api.category;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "status")
    public CategoryStatus status;
}
