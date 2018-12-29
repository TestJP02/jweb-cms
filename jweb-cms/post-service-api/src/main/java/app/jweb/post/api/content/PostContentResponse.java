package app.jweb.post.api.content;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostContentResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "postId")
    public String postId;
    @XmlElement(name = "content")
    public String content;
}
