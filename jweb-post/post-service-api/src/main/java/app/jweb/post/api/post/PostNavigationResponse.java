package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostNavigationResponse {
    @XmlElement(name = "prev")
    public PostResponse prev;
    @XmlElement(name = "next")
    public PostResponse next;
}
