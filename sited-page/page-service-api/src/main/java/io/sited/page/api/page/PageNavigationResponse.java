package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageNavigationResponse {
    @XmlElement(name = "prev")
    public PageResponse prev;
    @XmlElement(name = "next")
    public PageResponse next;
}
