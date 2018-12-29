package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostRelatedQuery {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "limit")
    public Integer limit;
}
