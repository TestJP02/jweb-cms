package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TopFixedPostQuery {
    @XmlElement(name = "page")
    public Integer page = 1;

    @XmlElement(name = "limit")
    public Integer limit = 10;
}
