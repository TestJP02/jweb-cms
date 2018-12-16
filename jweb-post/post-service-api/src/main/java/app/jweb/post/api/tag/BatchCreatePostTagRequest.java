package app.jweb.post.api.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchCreatePostTagRequest {
    @XmlElement(name = "tags")
    public List<PostTagView> tags;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
