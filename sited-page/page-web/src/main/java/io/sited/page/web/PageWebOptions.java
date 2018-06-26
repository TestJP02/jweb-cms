package io.sited.page.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageWebOptions {
    @XmlElement(name = "theme")
    public String theme;

    @XmlElement(name = "maxSitemapEntries")
    public Integer maxSitemapEntries = 40000;

    @XmlElement(name = "visitorCommentEnabled")
    public Boolean visitorCommentEnabled = false;
}
