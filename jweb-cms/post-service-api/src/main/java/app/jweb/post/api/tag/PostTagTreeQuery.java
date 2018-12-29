package app.jweb.post.api.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostTagTreeQuery {
    @XmlElement(name = "parentId")
    public String parentId;

    @XmlElement(name = "level")
    public Integer level;

    @XmlElement(name = "status")
    public PostTagStatus status;
}
