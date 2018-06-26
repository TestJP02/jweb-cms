package io.sited.page.tracking.admin.web.ajax.tracking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PageMostVisitAJAXResponse {
    @XmlElement(name = "pageId")
    public String pageId;

    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "path")
    public String path;

    @XmlElement(name = "total")
    public Integer total;
}
