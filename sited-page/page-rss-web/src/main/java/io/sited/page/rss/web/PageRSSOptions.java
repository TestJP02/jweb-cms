package io.sited.page.rss.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageRSSOptions {
    @XmlElement(name = "displayCount")
    public Integer displayCount = 20;
}
