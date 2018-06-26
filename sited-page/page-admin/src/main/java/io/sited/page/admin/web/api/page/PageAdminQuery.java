package io.sited.page.admin.web.api.page;

import io.sited.page.api.page.PageStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageAdminQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "desc")
    public Boolean desc = true;
    @XmlElement(name = "sortingField")
    public String sortingField = "updatedTime";
    @XmlElement(name = "status")
    public PageStatus status;
}
