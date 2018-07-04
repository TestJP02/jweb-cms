package io.sited.page.api.category;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryTreeQuery {
    @XmlElement(name = "parentId")
    public String parentId;

    @XmlElement(name = "depth")
    public Integer depth;

    @XmlElement(name = "status")
    public CategoryStatus status;
}
