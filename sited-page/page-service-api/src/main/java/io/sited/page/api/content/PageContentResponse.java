package io.sited.page.api.content;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageContentResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "content")
    public String content;
}
