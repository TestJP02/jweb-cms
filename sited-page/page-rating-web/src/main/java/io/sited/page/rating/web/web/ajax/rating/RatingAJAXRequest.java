package io.sited.page.rating.web.web.ajax.rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingAJAXRequest {
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "score")
    public Integer score;
    @XmlElement(name = "content")
    public String content;
}
